package com.education.education.user.service;

import com.education.education.user.domain.User;
import com.education.education.user.dto.UpdateProfileRequest;
import com.education.education.user.dto.UserResponse;
import com.education.education.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getProfile(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(String loginId, UpdateProfileRequest request) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getMajor() != null) {
            user.setMajor(request.getMajor());
        }
        return toResponse(user);
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getLoginId(),
                user.getName(),
                user.getStudentId(),
                user.getPhone(),
                user.getMajor(),
                user.getRole()
        );
    }
}
