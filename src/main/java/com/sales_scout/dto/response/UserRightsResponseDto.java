package com.sales_scout.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserRightsResponseDto {
    private Long userId;
    private List<Long> rightsId;

}
