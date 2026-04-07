package com.education.education.user.service;

import com.education.education.common.ApiResponse;
import com.education.education.security.JwtProperties;
import com.education.education.security.jwt.JwtTokenProvider;
import com.education.education.security.UserPrincipal;
import com.education.education.user.domain.RoleType;
import com.education.education.user.domain.User;
import com.education.education.auth.dto.LoginRequest;
import com.education.education.auth.dto.LoginResponse;
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
        
        User newUser = new User(request.getLoginId(), passwordEncoder.encode(request.getPassword()), request.getName(), request.getStudentId(), RoleType.USER);

        return userRepository.save(newUser);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword())
        );

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User currentUser = userRepository.findByLoginId(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse userResponse = new UserResponse(currentUser.getLoginId(), currentUser.getName(), currentUser.getStudentId(), currentUser.getRole());

        return new LoginResponse(accessToken, refreshToken, userResponse);
    }
}
