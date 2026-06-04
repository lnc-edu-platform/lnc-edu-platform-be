package com.education.education.volunteer.dto;

import com.education.education.preference.domain.Subject;
import com.education.education.preference.domain.TargetAudience;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

public class VolunteerRequest {

    @NotBlank
    private String title;

    private String description;
    private String location;
    private String address;
    private LocalDate activityDate;
    private Integer maxParticipants;
    private List<TargetAudience> targetAudiences;
    private List<Subject> subjects;

    public VolunteerRequest() {}

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
