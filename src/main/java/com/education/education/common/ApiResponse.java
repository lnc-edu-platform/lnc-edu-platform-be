package com.education.education.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Boolean success;
    private T data;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    // Static factory methods for convenience
    public static <T> ApiResponse<T> ofSuccess(T data) {
        return new ApiResponse<>(true, data, "Operation successful", LocalDateTime.now());
    }

    public static <T> ApiResponse<T> ofSuccess(T data, String message) {
        return new ApiResponse<>(true, data, message, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> ofError(String message) {
        return new ApiResponse<>(false, null, message, LocalDateTime.now());
    }

     public static <T> ApiResponse<T> ofError(String message, String errorMessage) {
        return new ApiResponse<>(false, null, errorMessage, LocalDateTime.now());
    }
}
