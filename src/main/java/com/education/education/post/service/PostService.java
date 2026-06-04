package com.education.education.post.service;

import com.education.education.post.domain.Post;
import com.education.education.post.dto.PostRequest;
import com.education.education.post.dto.PostResponse;
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
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse createPost(PostRequest request) {
        User author = getCurrentUser();
        Boolean isAnonymous = request.getIsAnonymous() != null ? request.getIsAnonymous() : false;
        Post post = new Post(request.getTitle(), request.getContent(), request.getCategory(), isAnonymous, author);
        return toResponse(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        return toResponse(findPostOrThrow(id));
    }

    @Transactional
    public PostResponse updatePost(Long id, PostRequest request) {
        Post post = findPostOrThrow(id);
        checkAuthor(post);
        Boolean isAnonymous = request.getIsAnonymous() != null ? request.getIsAnonymous() : post.getIsAnonymous();
        post.update(request.getTitle(), request.getContent(), request.getCategory(), isAnonymous);
        return toResponse(post);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = findPostOrThrow(id);
        checkAuthor(post);
        postRepository.delete(post);
    }

    private Post findPostOrThrow(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
    }

    private User getCurrentUser() {
        UserPrincipal principal = getCurrentPrincipal();
        return userRepository.findByLoginId(principal.getLoginId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private void checkAuthor(Post post) {
        UserPrincipal principal = getCurrentPrincipal();
        if (!post.getAuthor().getLoginId().equals(principal.getLoginId())) {
            throw new AccessDeniedException("작성자만 수정/삭제할 수 있습니다.");
        }
    }

    private UserPrincipal getCurrentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof UserPrincipal)) {
            throw new SecurityException("User not authenticated");
        }
        return (UserPrincipal) auth.getPrincipal();
    }

    private PostResponse toResponse(Post post) {
        UserResponse author = new UserResponse(
                post.getAuthor().getLoginId(),
                post.getAuthor().getName(),
                post.getAuthor().getStudentId(),
                post.getAuthor().getPhone(),
                post.getAuthor().getMajor(),
                post.getAuthor().getRole()
        );
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCategory(),
                post.getIsAnonymous(),
                author,
                post.getComments().size(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
