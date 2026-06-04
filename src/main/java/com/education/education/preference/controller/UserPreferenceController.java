package com.education.education.preference.controller;

import com.education.education.common.ApiResponse;
import com.education.education.preference.dto.PreferenceRequest;
import com.education.education.preference.dto.PreferenceResponse;
import com.education.education.preference.service.UserPreferenceService;
import com.education.education.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me/preferences")
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    @GetMapping
    public ResponseEntity<ApiResponse<PreferenceResponse>> getPreferences(
            @AuthenticationPrincipal UserPrincipal principal) {
        PreferenceResponse response = userPreferenceService.getPreferences(principal.getLoginId());
        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<PreferenceResponse>> savePreferences(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody PreferenceRequest request) {
        PreferenceResponse response = userPreferenceService.savePreferences(principal.getLoginId(), request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }
}
