package com.education.education.user.service;

import com.education.education.security.JwtProvider;
import com.education.education.user.domain.User;
import com.education.education.user.dto.LoginRequest;
import com.education.education.user.dto.LoginResponse;
import com.education.education.user.dto.SignupRequest;
import com.education.education.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public Long signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일");
        }

        User user = new User(
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                req.getName()
        );

        userRepository.save(user);
        return user.getId();
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호 불일치");
        }

        String token = jwtProvider.createToken(user.getId(), user.getEmail());
        return new LoginResponse(token, user.getId());
    }
}
