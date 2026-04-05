package com.education.education.reflection.domain;

import com.education.education.common.domain.BaseTimeEntity;
import com.education.education.comment.domain.Comment;
import com.education.education.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reflections")
public class Reflection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false, length = 100)
    private String volunteerPlace;

    @Column(nullable = false)
    private Boolean isAnonymous = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "reflection", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public Reflection(String title, String content, String volunteerPlace, Boolean isAnonymous, User author) {
        this.title = title;
        this.content = content;
        this.volunteerPlace = volunteerPlace;
        this.isAnonymous = isAnonymous;
        this.author = author;
    }
}
