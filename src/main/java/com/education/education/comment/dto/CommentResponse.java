package com.education.education.comment.dto;

import com.education.education.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private UserResponse author;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
