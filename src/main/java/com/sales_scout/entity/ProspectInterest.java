package com.sales_scout.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sales_scout.entity.data.*;
import com.sales_scout.enums.ActiveInactiveEnum;
import com.sales_scout.enums.ProspectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Entity
@Table(name = "prospectInterest")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProspectInterest extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "prospect_interest_mapping", // Nom de la table de jointure
            joinColumns = @JoinColumn(name = "interest_id"), // Clé étrangère vers ProspectInterest
            inverseJoinColumns = @JoinColumn(name = "prospect_id") // Clé étrangère vers Prospect
    )
    private List<Prospect> prospects;
}
