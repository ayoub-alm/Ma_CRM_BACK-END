package com.sales_scout.dto.response;


import com.sales_scout.dto.BaseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class InterestResponseDto extends BaseDto {
    private Long id;
    private String name;
    private Boolean status;
    private Long companyId;

}
