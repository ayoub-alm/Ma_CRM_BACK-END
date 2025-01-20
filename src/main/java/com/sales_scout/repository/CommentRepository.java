package com.sales_scout.repository;

import com.sales_scout.entity.Comment;
import com.sales_scout.enums.EntityEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Get non soft-deleted comments by module name and ID.
     * @param moduleType the module type of comments (like Invoice)
     * @param moduleReferenceId the ID of the associated entity
     * @return List<Comment> retrieved Comment
     */
    List<Comment> findByEntityAndEntityIdAndDeletedAtIsNull(EntityEnum moduleType, Long moduleReferenceId);

    Optional<Comment> findByIdAndDeletedAtIsNull(Long commentId);

    Optional<Comment> findByIdAndDeletedAtIsNotNull(Long commentId);
}
