package com.education.education.reflection.controller;

import com.education.education.common.ApiResponse;
import com.education.education.reflection.dto.ReflectionRequest;
import com.education.education.reflection.dto.ReflectionResponse;
import com.education.education.reflection.service.ReflectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reflections")
public class ReflectionController {

    private final ReflectionService reflectionService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReflectionResponse>> createReflection(@Valid @RequestBody ReflectionRequest request) {
        ReflectionResponse createdReflection = reflectionService.createReflection(request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(createdReflection, "Reflection created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReflectionResponse>> getReflectionById(@PathVariable Long id) {
        ReflectionResponse reflection = reflectionService.getReflectionById(id);
        return ResponseEntity.ok(ApiResponse.ofSuccess(reflection));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReflectionResponse>>> getAllReflections() {
        List<ReflectionResponse> reflections = reflectionService.getAllReflections();
        return ResponseEntity.ok(ApiResponse.ofSuccess(reflections));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReflectionResponse>> updateReflection(@PathVariable Long id, @Valid @RequestBody ReflectionRequest request) {
        ReflectionResponse updatedReflection = reflectionService.updateReflection(id, request);
        return ResponseEntity.ok(ApiResponse.ofSuccess(updatedReflection, "Reflection updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReflection(@PathVariable Long id) {
        reflectionService.deleteReflection(id);
        return ResponseEntity.ok(ApiResponse.ofSuccess(null, "Reflection deleted successfully"));
    }
}
