package com.education.education.preference.domain;

public enum Subject {
    SCRATCH("스크래치"),
    PYTHON("파이썬"),
    JAVA("자바"),
    WEB("웹 개발"),
    APP("앱 개발"),
    ALGORITHM("알고리즘"),
    C("C 언어"),
    CPP("C++"),
    AI("인공지능"),
    DATA("데이터 분석");

    private final String displayName;

    Subject(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
