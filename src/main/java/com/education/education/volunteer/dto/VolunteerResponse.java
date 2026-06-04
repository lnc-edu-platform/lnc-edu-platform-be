package com.education.education.volunteer.dto;

import com.education.education.preference.domain.Subject;
import com.education.education.preference.domain.TargetAudience;
import com.education.education.volunteer.domain.Volunteer;
import java.time.LocalDate;
import java.util.List;

public class VolunteerResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String address;
    private LocalDate activityDate;
    private Integer maxParticipants;
    private List<TargetAudience> targetAudiences;
    private List<Subject> subjects;

    public VolunteerResponse() {}

    public static VolunteerResponse from(Volunteer v) {
        VolunteerResponse r = new VolunteerResponse();
        r.id = v.getId();
        r.title = v.getTitle();
        r.description = v.getDescription();
        r.location = v.getLocation();
        r.address = v.getAddress();
        r.activityDate = v.getActivityDate();
        r.maxParticipants = v.getMaxParticipants();
        r.targetAudiences = v.getTargetAudiences();
        r.subjects = v.getSubjects();
        return r;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getAddress() { return address; }
    public LocalDate getActivityDate() { return activityDate; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public List<TargetAudience> getTargetAudiences() { return targetAudiences; }
    public List<Subject> getSubjects() { return subjects; }
}
