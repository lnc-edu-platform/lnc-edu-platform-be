package com.education.education.common;

import com.education.education.preference.domain.Subject;
import com.education.education.preference.domain.TargetAudience;
import com.education.education.volunteer.domain.Volunteer;
import com.education.education.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final VolunteerRepository volunteerRepository;

    @Override
    public void run(String... args) {
        if (volunteerRepository.count() > 0) return;

        volunteerRepository.saveAll(List.of(
                volunteer(
                        "성광중학교 코딩 멘토링",
                        "대구 북구에 위치한 중학교. 매주 토요일 자율동아리 형태로 html/css 웹 코딩 멘토링을 진행해요. 학생 16명, 멘토 8명 정원.",
                        "대구 북구",
                        "대구광역시 북구 검단로 150",
                        LocalDate.of(2026, 7, 5),
                        8,
                        List.of(TargetAudience.MIDDLE),
                        List.of(Subject.WEB)
                ),
                volunteer(
                        "칠성초등학교 스크래치 코딩",
                        "대구 북구 침산동에 위치한 초등학교. 매주 수요일 정규수업 시간에 스크래치 블록코딩을 중심으로 4학년 수업을 진행합니다.",
                        "대구 북구",
                        "대구광역시 북구 침산남로32길 17",
                        LocalDate.of(2026, 7, 9),
                        6,
                        List.of(TargetAudience.ELEMENTARY),
                        List.of(Subject.SCRATCH)
                ),
                volunteer(
                        "경북여자고등학교 파이썬 봉사",
                        "파이썬 기초 문법과 알고리즘 사고력을 키우는 수업을 매주 목요일에 진행합니다.",
                        "대구 중구",
                        "대구광역시 중구 중앙대로 288",
                        LocalDate.of(2026, 7, 10),
                        6,
                        List.of(TargetAudience.HIGH),
                        List.of(Subject.PYTHON)
                ),
                volunteer(
                        "신암지역아동센터 코딩 교육",
                        "대구 동구에 위치한 지역아동센터. 엔트리(블록코딩)를 활용해 초등학교 5학년 아이들과 기초 코딩 교육을 진행합니다.",
                        "대구 동구",
                        "대구광역시 동구 신암3동 210-13",
                        LocalDate.of(2026, 7, 8),
                        6,
                        List.of(TargetAudience.ELEMENTARY),
                        List.of(Subject.SCRATCH)
                ),
                volunteer(
                        "경운초등학교 파이썬/앱 개발",
                        "파이썬과 앱인벤터를 활용한 코딩 수업을 매주 수요일에 진행합니다.",
                        "대구 서구",
                        "대구광역시 서구 평리로54길 16",
                        LocalDate.of(2026, 7, 9),
                        4,
                        List.of(TargetAudience.ELEMENTARY),
                        List.of(Subject.PYTHON, Subject.APP)
                ),
                volunteer(
                        "산격중학교 코딩 멘토링",
                        "파이썬 기초 개념부터 함께 배우는 멘토링 수업입니다.",
                        "대구 북구",
                        "미정",
                        LocalDate.of(2026, 7, 12),
                        6,
                        List.of(TargetAudience.MIDDLE),
                        List.of(Subject.PYTHON)
                )
        ));
    }

    private Volunteer volunteer(String title, String description, String location, String address,
                                LocalDate activityDate, int maxParticipants,
                                List<TargetAudience> audiences, List<Subject> subjects) {
        Volunteer v = new Volunteer();
        v.setTitle(title);
        v.setDescription(description);
        v.setLocation(location);
        v.setAddress(address);
        v.setActivityDate(activityDate);
        v.setMaxParticipants(maxParticipants);
        v.setTargetAudiences(audiences);
        v.setSubjects(subjects);
        return v;
    }
}
