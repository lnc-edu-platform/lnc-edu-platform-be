package com.education.education.user.dto;

import com.education.education.user.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String loginId;
    private String name;
    private String studentId;
    private RoleType role;
}
