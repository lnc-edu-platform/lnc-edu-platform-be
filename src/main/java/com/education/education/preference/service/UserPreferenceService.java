package com.education.education.preference.service;

import com.education.education.preference.domain.UserPreference;
import com.education.education.preference.dto.PreferenceRequest;
import com.education.education.preference.dto.PreferenceResponse;
import com.education.education.preference.repository.UserPreferenceRepository;
import com.education.education.user.domain.User;
import com.education.education.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PreferenceResponse getPreferences(String loginId) {
        User user = findUser(loginId);
        UserPreference pref = userPreferenceRepository.findByUserId(user.getId())
                .orElse(new UserPreference(user));
        return new PreferenceResponse(pref.getTargetAudiences(), pref.getSubjects());
    }

    @Transactional
    public PreferenceResponse savePreferences(String loginId, PreferenceRequest request) {
        User user = findUser(loginId);
        UserPreference pref = userPreferenceRepository.findByUserId(user.getId())
                .orElse(new UserPreference(user));
        pref.setTargetAudiences(request.getTargetAudiences() != null ? request.getTargetAudiences() : pref.getTargetAudiences());
        pref.setSubjects(request.getSubjects() != null ? request.getSubjects() : pref.getSubjects());
        userPreferenceRepository.save(pref);
        return new PreferenceResponse(pref.getTargetAudiences(), pref.getSubjects());
    }

    private User findUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
