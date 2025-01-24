package com.sales_scout.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@Table(name = "rights")
public class Right extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToMany(mappedBy = "rights", fetch = FetchType.LAZY)
    private List<UserEntity> users; // Optional if bidirectional
}
