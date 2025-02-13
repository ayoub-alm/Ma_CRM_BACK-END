package com.sales_scout.service;

import com.sales_scout.dto.request.CommentRequestDto;
import com.sales_scout.dto.response.CommentResponseDto;
import com.sales_scout.entity.Comment;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.enums.EntityEnum;
import com.sales_scout.mapper.CommentMapper;
import com.sales_scout.repository.CommentRepository;
import com.sales_scout.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    /**
     * @param commentRequestDto
     * @return
     */
     public CommentResponseDto createComment(CommentRequestDto commentRequestDto)  {
        // Fetch the user
        UserEntity user = userRepository.findById(commentRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + commentRequestDto.getUserId()));

        // Create and save the comment
        Comment comment = Comment.builder()
                .commentTxt(commentRequestDto.getCommentTxt())
                .user(user)
                .entity(commentRequestDto.getEntity())
                .entityId(commentRequestDto.getEntityId())
                .build();

        return this.commentMapper.toDto(commentRepository.save(comment));
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
    public boolean softDeleteComment(Long commentId) throws EntityNotFoundException{
        Optional<Comment> comment = commentRepository.findByIdAndDeletedAtIsNull(commentId);
        if(comment.isPresent()){
            comment.get().setDeletedAt(LocalDateTime.now());
            commentRepository.save(comment.get());
        return true;
        }else {
            throw  new EntityNotFoundException("Comment with ID " + commentId + " not found or already deleted.");
        }
    }

    /**
     * This function allows us to restore a  comment
     *
     * @param commentId  the ID of the comment
     */
    public boolean restoreComment(Long commentId)throws EntityNotFoundException{
        Optional<Comment> comment = commentRepository.findByIdAndDeletedAtIsNotNull(commentId);
        if(comment.isPresent()){
            comment.get().setDeletedAt(null);
            commentRepository.save(comment.get());
            return true;
        }else {
            throw new EntityNotFoundException("Comment with Id "+ commentId + " not found or already restored");
        }
    }
}
