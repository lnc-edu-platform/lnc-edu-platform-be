package com.education.education.user.dto;

import com.education.education.user.domain.RoleType;

public class UserResponse {
    private String loginId;
    private String name;
    private String studentId;
    private RoleType role;

    public UserResponse() {}
    public UserResponse(String loginId, String name, String studentId, RoleType role) {
        this.loginId = loginId;
        this.name = name;
        this.studentId = studentId;
        this.role = role;
    }

    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public RoleType getRole() { return role; }
    public void setRole(RoleType role) { this.role = role; }
}
