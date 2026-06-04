package com.education.education.volunteer.domain;

import com.education.education.common.domain.BaseTimeEntity;
import com.education.education.preference.domain.Subject;
import com.education.education.preference.domain.TargetAudience;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "volunteers")
public class Volunteer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 200)
    private String location;

    @Column(length = 300)
    private String address;

    private LocalDate activityDate;

    private Integer maxParticipants;

    @ElementCollection
    @CollectionTable(name = "volunteer_target_audiences", joinColumns = @JoinColumn(name = "volunteer_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "target_audience")
    private List<TargetAudience> targetAudiences = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "volunteer_subjects", joinColumns = @JoinColumn(name = "volunteer_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "subject")
    private List<Subject> subjects = new ArrayList<>();

    public Volunteer() {}

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public LocalDate getActivityDate() { return activityDate; }
    public void setActivityDate(LocalDate activityDate) { this.activityDate = activityDate; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    public List<TargetAudience> getTargetAudiences() { return targetAudiences; }
    public void setTargetAudiences(List<TargetAudience> targetAudiences) { this.targetAudiences = targetAudiences; }
    public List<Subject> getSubjects() { return subjects; }
    public void setSubjects(List<Subject> subjects) { this.subjects = subjects; }
}
