package com.sales_scout.dto.request.create;


import com.sales_scout.dto.BaseDto;
import com.sales_scout.enums.InteractionSubject;
import com.sales_scout.enums.InteractionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InteractionRequestDto  {
    private Long customerId;
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
