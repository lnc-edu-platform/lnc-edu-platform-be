package com.education.education.resource.domain;

import com.education.education.common.domain.BaseTimeEntity;
import com.education.education.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "resources")
public class Resource extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 500)
    private String fileUrl;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", nullable = false)
    private User uploader;

    public Resource(String title, String fileUrl, String description, User uploader) {
        this.title = title;
        this.fileUrl = fileUrl;
        this.description = description;
        this.uploader = uploader;
    }
}
