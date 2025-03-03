package com.sales_scout.dto.EntityFilters;

import com.sales_scout.enums.InteractionSubject;
import com.sales_scout.enums.InteractionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InteractionFilterRequestDto {
    private Long companyId;
    private List<Long> customerIds;
    private List<Long> interlocutorIds;
    private List<Long> agentIds;
    private List<Long> affectedToIds;
    private List<InteractionSubject> interactionSubjects;
    private List<InteractionType> interactionTypes;
    private List<Long> updatedByIds;
    private List<Long> CreatedByIds;
    private LocalDateTime createdAtStart;
    private LocalDateTime createdAtEnd;
    private LocalDateTime updatedAtStart;
    private LocalDateTime updatedAtEnd;
    private String filterType; // "AND" or "OR"
}
