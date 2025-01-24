package com.sales_scout.entity;


import com.sales_scout.entity.leads.ProspectInterest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "interests")
@AllArgsConstructor @NoArgsConstructor
@Builder @Data
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
    private List<ProspectInterest> prospectInterests = new ArrayList<>();
}
