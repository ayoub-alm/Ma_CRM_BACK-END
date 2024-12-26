package com.sales_scout.dto.response.leads_dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InteractionCountDto {
    private String type; // Interaction type (e.g., email, call, meeting)
    private Long count;  // Count of interactions
}