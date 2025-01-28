package com.sales_scout.entity.leads;

import com.sales_scout.dto.response.InterestResponseDto;
import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Interest;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Entity
@Table(name = "prospectInterest")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProspectInterest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer; // Reference a single Prospect entity

    @ManyToOne
    @JoinColumn(name = "interest_id", nullable = false)
    private Interest interest; // Reference a single Inter
}
