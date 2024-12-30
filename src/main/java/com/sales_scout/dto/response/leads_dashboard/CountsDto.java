package com.sales_scout.dto.response.leads_dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder @Data
public class CountsDto {
    private Long countOfProspects;
    private Long CountOfInteractions;
    private Long CountOfInterlocutors;
}
