package com.sales_scout.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String commentTxt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // The user who created the comment.

    @Column(name = "module_type", nullable = false)
    private String entity; // Stores the module name (e.g., "Invoice", "Customer", etc.).

    @Column(name = "module_reference_id", nullable = false)
    private Long entityId; // Stores the ID of the associated entity.
}
