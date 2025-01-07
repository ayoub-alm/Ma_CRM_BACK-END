package com.sales_scout.dto.response;

import com.sales_scout.entity.Interest;
import com.sales_scout.entity.leads.Prospect;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class ProspectInterestResponseDto {

    private Long id;

    private String name;

    private Boolean status;

    private Long prospectId; // Reference a single Prospect entity

    private Long interestId; // Reference a single Inter
}
