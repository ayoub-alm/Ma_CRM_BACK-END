package com.sales_scout.dto.response;

import com.sales_scout.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter @Setter
@AllArgsConstructor
@SuperBuilder
public class RightsResponseDto extends BaseDto {
    private Long id;
    private String name;
    private String description;
    private Long companyId;

//    private List<UserRights> userRights;
}
