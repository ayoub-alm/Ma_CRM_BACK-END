package com.sales_scout.service;

import com.sales_scout.dto.request.CommentRequestDto;
import com.sales_scout.entity.Comment;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.repository.CommentRepository;
import com.sales_scout.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
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
                .entity(commentRequestDto.getEntity().toString())
                .entityId(commentRequestDto.getEntityId())
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
    public List<Comment> getCommentsByModule(String moduleType, Long moduleReferenceId) {
        System.out.println("*************************************test");
        return commentRepository.findByEntityAndEntityIdAndDeletedAtIsNull(moduleType, moduleReferenceId);
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
        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));

        comment.setCommentTxt(newContent);
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
