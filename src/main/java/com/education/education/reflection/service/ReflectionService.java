package com.education.education.reflection.service;

import com.education.education.reflection.domain.Reflection;
import com.education.education.reflection.dto.ReflectionRequest;
import com.education.education.reflection.dto.ReflectionResponse;
import com.education.education.reflection.repository.ReflectionRepository;
import com.education.education.security.jwt.JwtTokenProvider;
import com.education.education.security.UserPrincipal;
import com.education.education.user.domain.User;
import com.education.education.user.dto.UserResponse;
import com.education.education.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
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

        Reflection reflection = new Reflection();
        reflection.setTitle(request.getTitle());
        reflection.setContent(request.getContent());
        reflection.setAuthor(author);

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
        return reflectionRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReflectionResponse updateReflection(Long id, ReflectionRequest request) {
        Reflection reflection = reflectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reflection not found with id: " + id));

        checkAuthor(reflection);

        reflection.setTitle(request.getTitle());
        reflection.setContent(request.getContent());

        Reflection updatedReflection = reflectionRepository.save(reflection);
        return convertToResponse(updatedReflection);
    }

    @Transactional
    public void deleteReflection(Long id) {
        Reflection reflection = reflectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reflection not found with id: " + id));

        checkAuthor(reflection);

        reflectionRepository.delete(reflection);
    }

    private ReflectionResponse convertToResponse(Reflection reflection) {
        UserResponse userResponse = new UserResponse(
                reflection.getAuthor().getLoginId(),
                reflection.getAuthor().getName(),
                reflection.getAuthor().getStudentId(),
                reflection.getAuthor().getRole()
        );
        return new ReflectionResponse(
                reflection.getId(),
                reflection.getTitle(),
                reflection.getContent(),
                userResponse,
                reflection.getCreatedAt(),
                reflection.getUpdatedAt()
        );
    }

    private void checkAuthor(Reflection reflection) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new SecurityException("User not authenticated");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if (!reflection.getAuthor().getLoginId().equals(userPrincipal.getLoginId())) {
            throw new SecurityException("User is not authorized.");
        }
    }
}
