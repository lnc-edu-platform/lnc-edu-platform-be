package com.education.education.reflection.dto;

import com.education.education.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReflectionResponse {
    private Long id;
    private String title;
    private String content;
    private UserResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
