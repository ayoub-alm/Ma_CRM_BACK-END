package com.sales_scout.dto.response;

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
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionResponseDto extends BaseDto {
    private Long id;
    private Long prospectId;
    private String prospectName;
    private Long interlocutorId;
    private String interlocutorName;
    private String report;
    private InteractionSubject interactionSubject;
    private InteractionType interactionType;
    private Long previousInteractionId;
    private LocalDateTime planningDate;
    private String joinFilePath;
    private String address;
    private Long agentId;
    private String agentName;
    private Long affectedToId;
    private String affectedToName;
}