package com.education.education.post.dto;

import com.education.education.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String category;
    private Boolean isAnonymous;
    private UserResponse author;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
