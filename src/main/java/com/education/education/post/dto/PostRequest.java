package com.education.education.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private String category;

    private Boolean isAnonymous = false;
}
