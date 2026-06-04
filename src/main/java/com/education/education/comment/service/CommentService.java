package com.education.education.comment.service;

import com.education.education.comment.domain.Comment;
import com.education.education.comment.dto.CommentRequest;
import com.education.education.comment.dto.CommentResponse;
import com.education.education.comment.repository.CommentRepository;
import com.education.education.post.domain.Post;
import com.education.education.post.repository.PostRepository;
import com.education.education.security.UserPrincipal;
import com.education.education.user.domain.User;
import com.education.education.user.dto.UserResponse;
import com.education.education.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
        User author = getCurrentUser();
        Comment comment = new Comment(request.getContent(), author, post, null);
        return toResponse(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new EntityNotFoundException("Post not found with id: " + postId);
        }
        return commentRepository.findByPostId(postId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));
        checkAuthor(comment);
        commentRepository.delete(comment);
    }

    private User getCurrentUser() {
        UserPrincipal principal = getCurrentPrincipal();
        return userRepository.findByLoginId(principal.getLoginId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private void checkAuthor(Comment comment) {
        UserPrincipal principal = getCurrentPrincipal();
        if (!comment.getAuthor().getLoginId().equals(principal.getLoginId())) {
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }
    }

    private UserPrincipal getCurrentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof UserPrincipal)) {
            throw new SecurityException("User not authenticated");
        }
        return (UserPrincipal) auth.getPrincipal();
    }

    private CommentResponse toResponse(Comment comment) {
        UserResponse author = new UserResponse(
                comment.getAuthor().getLoginId(),
                comment.getAuthor().getName(),
                comment.getAuthor().getStudentId(),
                comment.getAuthor().getPhone(),
                comment.getAuthor().getMajor(),
                comment.getAuthor().getRole()
        );
        Long postId = comment.getPost() != null ? comment.getPost().getId() : null;
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                author,
                postId,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
