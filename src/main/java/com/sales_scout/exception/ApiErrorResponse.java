package com.sales_scout.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Map;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    LocalDateTime timestamp;
    int status;
    String error;
    String message;
    String path;
    Map<String, Object> details;
}
