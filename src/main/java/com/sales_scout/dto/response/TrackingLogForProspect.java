package com.sales_scout.dto.response;

import com.sales_scout.dto.BaseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
public class TrackingLogForProspect {
    private Long id;
    private String actionType; // e.g., CREATE, UPDATE, DELETE, VIEW
    private LocalDateTime timestamp;
    private UserResponseDto user;
    private String details;
}
