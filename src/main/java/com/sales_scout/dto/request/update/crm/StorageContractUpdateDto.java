package com.sales_scout.dto.request.update.crm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class StorageContractUpdateDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate initialDate;
    private int noticePeriod;
    private Double declaredValueOfStock;
    private Double insuranceValue;
}
