package com.sales_scout.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "user_rights")
@Builder
public class UserRights extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "rights_id", nullable = false)
    private Right right;
}
