package com.sales_scout.entity;

import com.sales_scout.entity.leads.Prospect;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interests")
@AllArgsConstructor @NoArgsConstructor
@Builder @Data
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "prospect_id")
    private Prospect prospect;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
