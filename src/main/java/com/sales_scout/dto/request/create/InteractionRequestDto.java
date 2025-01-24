package com.sales_scout.dto.request.create;

import com.sales_scout.enums.InteractionSubject;
import com.sales_scout.enums.InteractionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InteractionRequestDto {
    private Long prospectId;
    private Long interlocutorId;
    private String report;
    private InteractionSubject interactionSubject;
    private InteractionType interactionType;
    private Long previousInteractionId;
    private LocalDateTime planningDate;
    private String joinFilePath;
    private String address;
    private Long agentId;
    private Long affectedToId;
}
