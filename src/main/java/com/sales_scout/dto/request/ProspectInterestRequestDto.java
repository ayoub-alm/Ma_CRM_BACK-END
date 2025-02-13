package com.sales_scout.dto.request;

import com.sales_scout.entity.Interest;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProspectInterestRequestDto {


    private String name;

    private Boolean status;

    private Long prospectId; // Reference a single Prospect entity

    private Long interestId; // Reference a single Inter
}
