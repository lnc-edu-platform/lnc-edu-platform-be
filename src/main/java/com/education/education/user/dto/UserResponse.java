package com.education.education.user.dto;

import com.education.education.user.domain.RoleType;

public class UserResponse {
    private String loginId;
    private String name;
    private String studentId;
    private String phone;
    private String major;
    private RoleType role;

    public UserResponse() {}
    public UserResponse(String loginId, String name, String studentId, String phone, String major, RoleType role) {
        this.loginId = loginId;
        this.name = name;
        this.studentId = studentId;
        this.phone = phone;
        this.major = major;
        this.role = role;
    }

    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public RoleType getRole() { return role; }
    public void setRole(RoleType role) { this.role = role; }
}
