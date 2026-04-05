package com.education.education.comment.domain;

import com.education.education.common.domain.BaseTimeEntity;
import com.education.education.post.domain.Post;
import com.education.education.reflection.domain.Reflection;
import com.education.education.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reflection_id", nullable = true)
    private Reflection reflection;

    public Comment(String content, User author, Post post, Reflection reflection) {
        this.content = content;
        this.author = author;
        this.post = post;
        this.reflection = reflection;
    }
}
