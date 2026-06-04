package com.education.education.preference.domain;

import com.education.education.user.domain.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_preferences")
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ElementCollection
    @CollectionTable(name = "preference_target_audiences", joinColumns = @JoinColumn(name = "preference_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "target_audience")
    private List<TargetAudience> targetAudiences = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "preference_subjects", joinColumns = @JoinColumn(name = "preference_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "subject")
    private List<Subject> subjects = new ArrayList<>();

    public UserPreference() {}

    public UserPreference(User user) {
        this.user = user;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public List<TargetAudience> getTargetAudiences() { return targetAudiences; }
    public void setTargetAudiences(List<TargetAudience> targetAudiences) { this.targetAudiences = targetAudiences; }
    public List<Subject> getSubjects() { return subjects; }
    public void setSubjects(List<Subject> subjects) { this.subjects = subjects; }
}
