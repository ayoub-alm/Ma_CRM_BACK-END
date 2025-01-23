package com.sales_scout.dto.response.leads_dashboard;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.enums.ProspectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ProspectCountDto extends BaseDto {
    private ProspectStatus status;
    private Long count;

    public ProspectCountDto(ProspectStatus status, Long count) {
        this.status = status;
        this.count = count;
    }

    public ProspectCountDto() {}
}
