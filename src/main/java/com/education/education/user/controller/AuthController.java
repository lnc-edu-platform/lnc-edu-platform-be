package com.education.education.user.controller;

import com.education.education.common.ApiResponse;
import com.education.education.auth.dto.LoginRequest;
import com.education.education.auth.dto.LoginResponse;
import com.education.education.user.dto.SignupRequest;
import com.education.education.user.dto.UserResponse;
import com.education.education.user.service.AuthService;
import com.education.education.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signup(@Valid @RequestBody SignupRequest request) {
        User newUser = authService.signup(request);
        UserResponse userResponse = new UserResponse(newUser.getLoginId(), newUser.getName(), newUser.getStudentId(), newUser.getPhone(), newUser.getMajor(), newUser.getRole());
        return ResponseEntity.ok(ApiResponse.ofSuccess(userResponse, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(loginResponse, "Login successful"));
    }
}
