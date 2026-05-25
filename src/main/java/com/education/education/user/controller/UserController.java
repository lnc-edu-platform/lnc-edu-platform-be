package com.education.education.user.controller;

import com.education.education.common.ApiResponse;
import com.education.education.security.UserPrincipal;
import com.education.education.user.domain.User;
import com.education.education.user.dto.UserResponse;
import com.education.education.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(@AuthenticationPrincipal UserPrincipal principal) {
        User user = userRepository.findByLoginId(principal.getLoginId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        UserResponse response = new UserResponse(user.getLoginId(), user.getName(), user.getStudentId(), user.getRole());
        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }
}
