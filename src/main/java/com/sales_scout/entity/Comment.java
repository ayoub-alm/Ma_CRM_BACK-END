package com.sales_scout.entity;

import com.sales_scout.enums.EntityEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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


    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "module_type", nullable = false)
    private EntityEnum entity = EntityEnum.INCLASSED; // Stores the module name (e.g., "Invoice", "Customer", etc.).

    @Column(name = "module_reference_id", nullable = false)
    private Long entityId; // Stores the ID of the associated entity.
}
