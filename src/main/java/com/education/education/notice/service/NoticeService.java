package com.education.education.notice.service;

import com.education.education.notice.domain.Notice;
import com.education.education.notice.dto.NoticeRequest;
import com.education.education.notice.dto.NoticeResponse;
import com.education.education.notice.repository.NoticeRepository;
import com.education.education.security.UserPrincipal;
import com.education.education.user.domain.User;
import com.education.education.user.dto.UserResponse;
import com.education.education.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Transactional
    public NoticeResponse createNotice(NoticeRequest request) {
        User author = getCurrentUser();
        Notice notice = new Notice(request.getTitle(), request.getContent(), author);
        return toResponse(noticeRepository.save(notice));
    }

    @Transactional(readOnly = true)
    public List<NoticeResponse> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NoticeResponse getNoticeById(Long id) {
        return toResponse(findNoticeOrThrow(id));
    }

    @Transactional
    public NoticeResponse updateNotice(Long id, NoticeRequest request) {
        Notice notice = findNoticeOrThrow(id);
        notice.update(request.getTitle(), request.getContent());
        return toResponse(notice);
    }

    @Transactional
    public void deleteNotice(Long id) {
        noticeRepository.delete(findNoticeOrThrow(id));
    }

    private Notice findNoticeOrThrow(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notice not found with id: " + id));
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof UserPrincipal)) {
            throw new SecurityException("User not authenticated");
        }
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        return userRepository.findByLoginId(principal.getLoginId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private NoticeResponse toResponse(Notice notice) {
        UserResponse author = new UserResponse(
                notice.getAuthor().getLoginId(),
                notice.getAuthor().getName(),
                notice.getAuthor().getStudentId(),
                notice.getAuthor().getRole()
        );
        return new NoticeResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                author,
                notice.getCreatedAt(),
                notice.getUpdatedAt()
        );
    }
}
