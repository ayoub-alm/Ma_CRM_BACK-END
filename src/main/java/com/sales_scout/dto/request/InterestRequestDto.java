package com.sales_scout.dto.request;


import com.sales_scout.dto.BaseDto;
import com.sales_scout.entity.leads.Prospect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InterestRequestDto extends BaseDto {

    private String name;
    private Boolean status;
    private Long companyId;
    private List<Prospect> prospects;
}
