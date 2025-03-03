package com.sales_scout.entity.leads;

import com.sales_scout.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "customer_status")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerStatus extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private Long display_order ;
    private String color;
    private String backgroundColor;
    private String cssClass;
}
