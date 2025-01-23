package com.sales_scout.dto.response.leads_dashboard;

import com.sales_scout.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class CountsDto extends BaseDto {
    private Long countOfProspects;
    private Long CountOfInteractions;
    private Long CountOfInterlocutors;
}
