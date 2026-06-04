package com.education.education.user.controller;

import com.education.education.common.ApiResponse;
import com.education.education.security.UserPrincipal;
import com.education.education.user.dto.UpdateProfileRequest;
import com.education.education.user.dto.UserResponse;
import com.education.education.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        UserResponse response = userService.getProfile(principal.getLoginId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateProfileRequest request) {
        UserResponse response = userService.updateProfile(principal.getLoginId(), request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }
}
