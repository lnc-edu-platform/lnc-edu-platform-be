package com.education.education.post.controller;

import com.education.education.common.ApiResponse;
import com.education.education.post.dto.PostRequest;
import com.education.education.post.dto.PostResponse;
import com.education.education.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@Valid @RequestBody PostRequest request) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(postService.createPost(request), "Post created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        return ResponseEntity.ok(ApiResponse.ofSuccess(postService.getAllPosts()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(postService.getPostById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable Long id, @Valid @RequestBody PostRequest request) {
        return ResponseEntity.ok(ApiResponse.ofSuccess(postService.updatePost(id, request), "Post updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.ofSuccess(null, "Post deleted successfully"));
    }
}
