package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dimensions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Dimension extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double length;

    private Double width;

    private Double height;

    private Double volume;

    @OneToMany(mappedBy = "dimension", cascade = CascadeType.ALL)
    private Set<StockedItem> stockedItems;

}
