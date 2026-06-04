package com.education.education.volunteer.service;

import com.education.education.preference.domain.Subject;
import com.education.education.preference.domain.TargetAudience;
import com.education.education.preference.domain.UserPreference;
import com.education.education.preference.repository.UserPreferenceRepository;
import com.education.education.user.domain.User;
import com.education.education.user.repository.UserRepository;
import com.education.education.volunteer.domain.Volunteer;
import com.education.education.volunteer.dto.VolunteerRequest;
import com.education.education.volunteer.dto.VolunteerResponse;
import com.education.education.volunteer.repository.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<VolunteerResponse> getAll() {
        return volunteerRepository.findAll().stream()
                .map(VolunteerResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VolunteerResponse getById(Long id) {
        Volunteer v = volunteerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Volunteer not found: " + id));
        return VolunteerResponse.from(v);
    }

    @Transactional(readOnly = true)
    public List<VolunteerResponse> getRecommended(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Optional<UserPreference> prefOpt = userPreferenceRepository.findByUserId(user.getId());
        List<Volunteer> all = volunteerRepository.findAll();

        if (prefOpt.isEmpty()) {
            return all.stream().map(VolunteerResponse::from).collect(Collectors.toList());
        }

        UserPreference pref = prefOpt.get();
        List<TargetAudience> audiences = pref.getTargetAudiences();
        List<Subject> subjects = pref.getSubjects();

        if ((audiences == null || audiences.isEmpty()) && (subjects == null || subjects.isEmpty())) {
            return all.stream().map(VolunteerResponse::from).collect(Collectors.toList());
        }

        // score=0인 봉사처도 포함, 점수 높은 순 정렬
        return all.stream()
                .sorted(Comparator.comparingInt((Volunteer v) -> scoreVolunteer(v, audiences, subjects)).reversed())
                .map(VolunteerResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public VolunteerResponse create(VolunteerRequest request) {
        return VolunteerResponse.from(volunteerRepository.save(buildFromRequest(new Volunteer(), request)));
    }

    @Transactional
    public VolunteerResponse update(Long id, VolunteerRequest request) {
        Volunteer v = volunteerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Volunteer not found: " + id));
        return VolunteerResponse.from(volunteerRepository.save(buildFromRequest(v, request)));
    }

    @Transactional
    public void delete(Long id) {
        if (!volunteerRepository.existsById(id)) {
            throw new EntityNotFoundException("Volunteer not found: " + id);
        }
        volunteerRepository.deleteById(id);
    }

    private Volunteer buildFromRequest(Volunteer v, VolunteerRequest req) {
        v.setTitle(req.getTitle());
        v.setDescription(req.getDescription());
        v.setLocation(req.getLocation());
        v.setAddress(req.getAddress());
        v.setActivityDate(req.getActivityDate());
        v.setMaxParticipants(req.getMaxParticipants());
        v.setTargetAudiences(req.getTargetAudiences() != null ? req.getTargetAudiences() : new ArrayList<>());
        v.setSubjects(req.getSubjects() != null ? req.getSubjects() : new ArrayList<>());
        return v;
    }

    // targetAudience 가중치 2, subject 가중치 1
    private int scoreVolunteer(Volunteer v, List<TargetAudience> audiences, List<Subject> subjects) {
        int score = 0;
        if (audiences != null) {
            for (TargetAudience a : v.getTargetAudiences()) {
                if (audiences.contains(a)) score += 2;
            }
        }
        if (subjects != null) {
            for (Subject s : v.getSubjects()) {
                if (subjects.contains(s)) score += 1;
            }
        }
        return score;
    }
}
