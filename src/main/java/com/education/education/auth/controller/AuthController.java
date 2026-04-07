package com.education.education.auth.controller;

import com.education.education.common.ApiResponse;
import com.education.education.user.dto.UserResponse;
import com.education.education.auth.dto.LoginRequest;
import com.education.education.auth.dto.LoginResponse;
import com.education.education.user.dto.SignupRequest;
import com.education.education.user.service.AuthService;
import com.education.education.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signup(@Valid @RequestBody SignupRequest request) {
        User newUser = authService.signup(request);
        UserResponse userResponse = UserResponse.builder()
                .loginId(newUser.getLoginId())
                .name(newUser.getName())
                .studentId(newUser.getStudentId())
                .role(newUser.getRole())
                .build();
        return ResponseEntity.ok(ApiResponse.ofSuccess(userResponse, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(loginResponse, "Login successful"));
    }
}
