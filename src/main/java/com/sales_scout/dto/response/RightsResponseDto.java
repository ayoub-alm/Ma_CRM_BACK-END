package com.sales_scout.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
public class RightsResponseDto {
    private Long id;

    private String name;

    private String description;

//    private List<UserRights> userRights;
}
