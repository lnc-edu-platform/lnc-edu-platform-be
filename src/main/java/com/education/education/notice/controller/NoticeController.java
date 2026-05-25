package com.education.education.notice.controller;

import com.education.education.common.ApiResponse;
import com.education.education.notice.dto.NoticeRequest;
import com.education.education.notice.dto.NoticeResponse;
import com.education.education.notice.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NoticeResponse>> createNotice(@Valid @RequestBody NoticeRequest request) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(noticeService.createNotice(request), "Notice created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NoticeResponse>>> getAllNotices() {
        return ResponseEntity.ok(ApiResponse.ofSuccess(noticeService.getAllNotices()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NoticeResponse>> getNoticeById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(noticeService.getNoticeById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NoticeResponse>> updateNotice(@PathVariable Long id, @Valid @RequestBody NoticeRequest request) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(noticeService.updateNotice(id, request), "Notice updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.ok(ApiResponse.ofSuccess(null, "Notice deleted successfully"));
    }
}
