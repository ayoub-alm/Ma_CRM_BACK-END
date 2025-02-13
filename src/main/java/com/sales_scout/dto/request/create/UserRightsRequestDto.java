package com.sales_scout.dto.request.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserRightsRequestDto {
    private Long userId;
    private List<Long> rightsId;
}