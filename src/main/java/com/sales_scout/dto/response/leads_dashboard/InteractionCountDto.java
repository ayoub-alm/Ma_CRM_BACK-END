package com.sales_scout.dto.response.leads_dashboard;

import com.sales_scout.enums.InteractionSubject;
import com.sales_scout.enums.InteractionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class InteractionCountDto {
    private InteractionSubject subject;
    private InteractionType type;// Interaction type (e.g., email, call, meeting)
    private String createdBy;
    private Long count;  // Count of interactions
}