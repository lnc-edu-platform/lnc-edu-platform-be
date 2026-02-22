package com.education.education.user.controller;

import com.education.education.user.dto.LoginRequest;
import com.education.education.user.dto.LoginResponse;
import com.education.education.user.dto.SignupRequest;
import com.education.education.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public Long signup(@Valid @RequestBody SignupRequest req){
        return authService.signup(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req){
        return authService.login(req);
    }
}
