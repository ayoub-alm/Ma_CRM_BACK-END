package com.sales_scout.controller;

import com.sales_scout.dto.request.CommentRequestDto;
import com.sales_scout.dto.response.CommentResponseDto;
import com.sales_scout.entity.Comment;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Create a new comment.
     * @return the created comment
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto) {
        try{
            return ResponseEntity.ok( commentService.createComment(commentRequestDto));
        }
        catch (Exception e){
            throw  new ResourceNotFoundException(e.getMessage(),"comment",commentRequestDto);
        }
    }

    /**
     * Get all comments for a module.
     *
     * @param entity        the module type
     * @param entityId the ID of the associated module entity
     * @return a list of comments
     */
    @GetMapping("")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByModule(
            @RequestParam String entity,
            @RequestParam Long entityId) {
        try{
            List<CommentResponseDto> comments = commentService.getCommentsByModule(entity, entityId);
            return ResponseEntity.ok(comments);
        }
        catch (Exception e){
            throw  new ResourceNotFoundException(e.getMessage(),"comment",entity);
        }

    }

    /**
     * Get a comment by ID.
     *
     * @param commentId the ID of the comment
     * @return the comment, if found
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long commentId) {
        try{
            Optional<Comment> comment = commentService.getCommentById(commentId);
            return comment.map(ResponseEntity::ok).orElseThrow(() -> new EntityNotFoundException("comment not found"));
        }
        catch (Exception e){
            throw  new EntityNotFoundException(e.getMessage());
        }
    }

    /**
     * Update a comment's content.
     *
     * @param commentId the ID of the comment
     * @param newContent the new content
     * @return the updated comment
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long commentId,
            @RequestParam String newContent) {

        Comment updatedComment = commentService.updateComment(commentId, newContent);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * Soft-delete a comment by ID.
     *
     * @param commentId the ID of the comment
     * @return a response indicating the result
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Boolean> softDeleteComment(@PathVariable Long commentId)throws EntityNotFoundException {
        try{
        return  ResponseEntity.ok().body(commentService.softDeleteComment(commentId));
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    /**
     * Restore a soft-deleted comment.
     *
     * @param commentId the ID of the comment
     * @return a response indicating the result
     */
    @PutMapping("/restore/{commentId}")
    public ResponseEntity<Boolean> restoreComment(@PathVariable Long commentId)throws EntityNotFoundException {
        try{
            return  ResponseEntity.ok().body(commentService.restoreComment(commentId));
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }
}
