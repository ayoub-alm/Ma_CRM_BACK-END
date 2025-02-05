package com.sales_scout.dto.response.leads_dashboard;

import com.sales_scout.dto.response.InterestResponseDto;
import com.sales_scout.entity.Interest;
import com.sales_scout.entity.data.City;
import com.sales_scout.entity.data.Industry;
import com.sales_scout.enums.ProspectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
public class CustomerCountDto {
    private ProspectStatus status;
    private InterestResponseDto interest;
    private String createdBy;
    private City city;
    private LocalDateTime createdAt;
    private Industry industry;
    private Long count;
}
