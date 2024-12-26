package com.sales_scout.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Builder
@Getter @Setter
public class TrackingLogForProspect {
    private Long id;
    private String actionType; // e.g., CREATE, UPDATE, DELETE, VIEW
    private LocalDateTime timestamp;
    private UserResponseDto user;
    private String details;
}
