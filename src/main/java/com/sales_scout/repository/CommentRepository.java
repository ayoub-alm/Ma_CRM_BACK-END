package com.sales_scout.repository;

import com.sales_scout.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Get non soft-deleted comments by module name and id
     * @param moduleType the module type of comments (like Invoice)
     * @param moduleReferenceId The id of element
     * @return  List<Comment> retrieved Comment
     */
    List<Comment> findByEntityAndEntityIdAndDeletedAtIsNull(String moduleType, Long moduleReferenceId);

    Optional<Comment> findByIdAndDeletedAtIsNull(Long commentId);
}
