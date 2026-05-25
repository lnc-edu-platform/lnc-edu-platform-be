package com.education.education.comment.controller;

import com.education.education.comment.dto.CommentRequest;
import com.education.education.comment.dto.CommentResponse;
import com.education.education.comment.service.CommentService;
import com.education.education.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(commentService.createComment(postId, request), "Comment created successfully"));
    }

    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(commentService.getCommentsByPostId(postId)));
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.ofSuccess(null, "Comment deleted successfully"));
    }
}
