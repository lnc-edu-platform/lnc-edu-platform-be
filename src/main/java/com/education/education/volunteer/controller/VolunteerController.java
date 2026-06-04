package com.education.education.volunteer.controller;

import com.education.education.common.ApiResponse;
import com.education.education.security.UserPrincipal;
import com.education.education.volunteer.dto.VolunteerRequest;
import com.education.education.volunteer.dto.VolunteerResponse;
import com.education.education.volunteer.service.VolunteerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/volunteers")
public class VolunteerController {

    private final VolunteerService volunteerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VolunteerResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ofSuccess(volunteerService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VolunteerResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(volunteerService.getById(id)));
    }

    @GetMapping("/recommended")
    public ResponseEntity<ApiResponse<List<VolunteerResponse>>> getRecommended(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(volunteerService.getRecommended(principal.getLoginId())));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VolunteerResponse>> create(@Valid @RequestBody VolunteerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ofSuccess(volunteerService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VolunteerResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody VolunteerRequest request) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(volunteerService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        volunteerService.delete(id);
        return ResponseEntity.ok(ApiResponse.ofSuccess(null));
    }
}
