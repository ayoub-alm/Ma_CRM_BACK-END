package com.sales_scout.entity;


import com.sales_scout.entity.leads.CustomerInterest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "interests")
@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter
public class Interest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "interest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerInterest> customerInterests = new ArrayList<>();
}
