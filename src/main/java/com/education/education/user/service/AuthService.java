package com.education.education.user.service;

import com.education.education.common.ApiResponse;
import com.education.education.security.JwtProperties;
import com.education.education.security.JwtTokenProvider;
import com.education.education.security.UserPrincipal;
import com.education.education.user.domain.RoleType;
import com.education.education.user.domain.User;
import com.education.education.user.dto.LoginRequest;
import com.education.education.user.dto.LoginResponse;
import com.education.education.user.dto.SignupRequest;
import com.education.education.user.dto.UserResponse;
import com.education.education.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public User signup(SignupRequest request) {
        if (userRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new IllegalArgumentException("Login ID already exists: " + request.getLoginId());
        }
        if (request.getStudentId() != null && !request.getStudentId().trim().isEmpty() && 
            userRepository.findByStudentId(request.getStudentId()).isPresent()) {
            throw new IllegalArgumentException("Student ID already exists: " + request.getStudentId());
        }

        User newUser = User.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .studentId(request.getStudentId())
                .role(RoleType.USER) // Default role
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(newUser);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        // Authenticate the user using Spring Security's AuthenticationManager
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword())
            );
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("Invalid login ID or password.");
        } catch (Exception e) {
            // Catch any other authentication exceptions
            throw new IllegalArgumentException("Authentication failed: " + e.getMessage());
        }

        // If authentication is successful, generate JWTs
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // Get UserDetails from the authenticated principal
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        // Construct UserResponse from UserPrincipal
        UserResponse userResponse = UserResponse.builder()
                .loginId(userPrincipal.getLoginId())
                .name(userPrincipal.getUsername())
                // Assuming UserPrincipal can be extended or user details fetched again if needed for more fields
                // For now, we'll just use loginId and role, assuming other fields like name/studentId are not readily available in UserPrincipal
                // If UserPrincipal needs more fields, it should be populated from the User entity during its creation.
                .role(RoleType.valueOf(userPrincipal.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")))
                .build();

        // Fetch full user details for UserResponse if UserPrincipal is minimal
        // This part might need adjustment based on how UserPrincipal is populated
        // For simplicity here, we assume UserPrincipal *contains* enough info or we fetch it again.
        // If UserPrincipal is minimal, we might need another repo call:
        User currentUser = userRepository.findByLoginId(userPrincipal.getLoginId()).orElseThrow(() -> new UsernameNotFoundException("User not found after authentication"));
        userResponse = UserResponse.builder()
                .loginId(currentUser.getLoginId())
                .name(currentUser.getName())
                .studentId(currentUser.getStudentId())
                .role(currentUser.getRole())
                .build();

        return new LoginResponse(accessToken, refreshToken, userResponse);
    }
}
