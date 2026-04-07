package com.education.education.reflection.service;

import com.education.education.common.ApiResponse;
import com.education.education.reflection.domain.Reflection;
import com.education.education.reflection.dto.ReflectionRequest;
import com.education.education.reflection.dto.ReflectionResponse;
import com.education.education.reflection.repository.ReflectionRepository;
import com.education.education.security.JwtTokenProvider;
import com.education.education.security.UserPrincipal;
import com.education.education.user.domain.User;
import com.education.education.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReflectionService {

    private final ReflectionRepository reflectionRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ReflectionResponse createReflection(ReflectionRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new SecurityException("User not authenticated");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User author = userRepository.findByLoginId(userPrincipal.getLoginId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        Reflection reflection = Reflection.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Reflection savedReflection = reflectionRepository.save(reflection);
        return convertToResponse(savedReflection);
    }

    @Transactional(readOnly = true)
    public ReflectionResponse getReflectionById(Long id) {
        Reflection reflection = reflectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reflection not found with id: " + id));
        return convertToResponse(reflection);
    }

    @Transactional(readOnly = true)
    public List<ReflectionResponse> getAllReflections() {
        List<Reflection> reflections = reflectionRepository.findAll();
        return reflections.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReflectionResponse updateReflection(Long id, ReflectionRequest request) {
        Reflection reflection = reflectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reflection not found with id: " + id));

        // Optional: Add authorization check to ensure only the author can update
        checkAuthor(reflection);

        reflection.setTitle(request.getTitle());
        reflection.setContent(request.getContent());
        reflection.setUpdatedAt(LocalDateTime.now());

        Reflection updatedReflection = reflectionRepository.save(reflection);
        return convertToResponse(updatedReflection);
    }

    @Transactional
    public void deleteReflection(Long id) {
        Reflection reflection = reflectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reflection not found with id: " + id));

        // Optional: Add authorization check to ensure only the author can delete
        checkAuthor(reflection);

        reflectionRepository.delete(reflection);
    }

    private ReflectionResponse convertToResponse(Reflection reflection) {
        return ReflectionResponse.builder()
                .id(reflection.getId())
                .title(reflection.getTitle())
                .content(reflection.getContent())
                .author(UserResponse.builder()
                        .loginId(reflection.getAuthor().getLoginId())
                        .name(reflection.getAuthor().getName())
                        .studentId(reflection.getAuthor().getStudentId())
                        .role(reflection.getAuthor().getRole())
                        .build())
                .createdAt(reflection.getCreatedAt())
                .updatedAt(reflection.getUpdatedAt())
                .build();
    }

    // Helper method to check if the current user is the author of the reflection
    private void checkAuthor(Reflection reflection) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new SecurityException("User not authenticated");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if (!reflection.getAuthor().getLoginId().equals(userPrincipal.getLoginId())) {
            throw new SecurityException("User is not authorized to perform this action on this reflection.");
        }
    }
}
