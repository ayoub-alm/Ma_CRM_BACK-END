package com.sales_scout.dto.response.leads_dashboard;

import com.sales_scout.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InteractionCountDto extends BaseDto {
    private String type; // Interaction type (e.g., email, call, meeting)
    private Long count;  // Count of interactions
}