package com.education.education.preference.domain;

public enum TargetAudience {
    ELEMENTARY("초등학생"),
    MIDDLE("중학생"),
    HIGH("고등학생"),
    UNIVERSITY("대학생");

    private final String displayName;

    TargetAudience(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
