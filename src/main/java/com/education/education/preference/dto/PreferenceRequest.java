package com.education.education.preference.dto;

import com.education.education.preference.domain.Subject;
import com.education.education.preference.domain.TargetAudience;
import java.util.List;

public class PreferenceRequest {
    private List<TargetAudience> targetAudiences;
    private List<Subject> subjects;

    public PreferenceRequest() {}

    public List<TargetAudience> getTargetAudiences() { return targetAudiences; }
    public void setTargetAudiences(List<TargetAudience> targetAudiences) { this.targetAudiences = targetAudiences; }
    public List<Subject> getSubjects() { return subjects; }
    public void setSubjects(List<Subject> subjects) { this.subjects = subjects; }
}
