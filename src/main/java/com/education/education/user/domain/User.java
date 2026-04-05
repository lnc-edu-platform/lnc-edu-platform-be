package com.education.education.user.domain;

import com.education.education.common.domain.BaseTimeEntity;
import com.education.education.comment.domain.Comment;
import com.education.education.post.domain.Post;
import com.education.education.reflection.domain.Reflection;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String studentId;

    @Column(nullable = true)
    private Integer grade;

    @Column(nullable = true, length = 100)
    private String major;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Reflection> reflections = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public User(String loginId, String passwordHash, String name, String studentId, Integer grade, String major, RoleType role) {
        this.loginId = loginId;
        this.passwordHash = passwordHash;
        this.name = name;
        this.studentId = studentId;
        this.grade = grade;
        this.major = major;
        this.role = role;
    }
}
