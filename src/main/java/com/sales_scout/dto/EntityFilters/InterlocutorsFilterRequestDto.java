package com.sales_scout.dto.EntityFilters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterlocutorsFilterRequestDto {
    private List<String> status;
    private List<Long> customersIds;
    private List<Long> departmentsIds;
    private List<Long> jobTitlesIds;
    private List<Long> createdByIds;
    private List<Long> updatedByIds;
    private LocalDateTime createdAtStart;
    private LocalDateTime createdAtEnd;
    private LocalDateTime updatedAtStart;
    private LocalDateTime updatedAtEnd;
    private Long companyId;
    private String filterType;
}
