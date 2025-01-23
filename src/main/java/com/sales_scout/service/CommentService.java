package com.sales_scout.service;

import com.sales_scout.dto.request.CommentRequestDto;
import com.sales_scout.dto.response.CommentResponseDto;
import com.sales_scout.entity.Comment;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.enums.EntityEnum;
import com.sales_scout.repository.CommentRepository;
import com.sales_scout.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, AuthenticationService authenticationService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    /**
     *
     * @param commentRequestDto
     * @return
     */
     public Comment createComment(CommentRequestDto commentRequestDto) {
         // Fetch the user
        UserEntity user = userRepository.findById(commentRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + commentRequestDto.getUserId()));

        // Create and save the comment
        Comment comment = Comment.builder()
                .commentTxt(commentRequestDto.getCommentTxt())
                .user(user)
                .entity(commentRequestDto.getEntity())
                .entityId(commentRequestDto.getEntityId())
                .createdAt(LocalDateTime.now())
                .createdBy(user.getName())
                .build();

        return commentRepository.save(comment);
    }

    /**
     * Get all comments by module type and reference ID.
     *
     * @param moduleType        the module type
     * @param moduleReferenceId the ID of the associated module entity
     * @return a list of comments
     */
    public List<CommentResponseDto> getCommentsByModule(String moduleType, Long moduleReferenceId) {
        EntityEnum entityEnum;
        try {
            entityEnum = EntityEnum.valueOf(moduleType); // Convert String to Enum
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid module type: " + moduleType);
        }

        List<Comment> comments = commentRepository.findByEntityAndEntityIdAndDeletedAtIsNull(entityEnum, moduleReferenceId);
        return comments.stream()
                .map(comment -> CommentResponseDto.builder()
                        .id(comment.getId())
                        .commentTxt(comment.getCommentTxt())
                        .userId(comment.getUser().getId())
                        .userName(comment.getUser().getName())
                        .entity(comment.getEntity())
                        .entityId(comment.getEntityId())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .createdBy(comment.getCreatedBy())
                        .updatedBy(comment.getUpdatedBy())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get a specific comment by ID.
     *
     * @param commentId the ID of the comment
     * @return the comment, if found
     */
    public Optional<Comment> getCommentById(Long commentId) {
        return commentRepository.findByIdAndDeletedAtIsNull(commentId);
    }

    /**
     * Update a comment's content.
     *
     * @param commentId the ID of the comment
     * @param newContent the new content for the comment
     * @return the updated comment
     */
    public Comment updateComment(Long commentId, String newContent) {

        UserEntity user = this.authenticationService.getCurrentUser();
        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));
        comment.setCommentTxt(newContent);
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setUpdatedBy(user.getName());
        return commentRepository.save(comment);
    }

    /**
     * Soft-delete  a comment by ID.
     *
     * @param commentId the ID of the comment
     */
    public void softDeleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));
        comment.setDeletedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    /**
     * This function allows us to restore a soft-deleted comment
     *
     * @param commentId  the ID of the comment
     */
    public void restoreComment(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));
        comment.setDeletedAt(null);
        commentRepository.save(comment);
    }
}
