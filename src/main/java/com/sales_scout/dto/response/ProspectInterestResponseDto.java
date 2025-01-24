package com.sales_scout.dto.response;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.entity.Interest;
import com.sales_scout.entity.leads.Prospect;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder

public class ProspectInterestResponseDto extends BaseDto {

    private Long id;

    private String name;

    private Boolean status;

    private Long prospectId; // Reference a single Prospect entity

    private Long interestId; // Reference a single Inter
}
