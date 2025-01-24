package com.sales_scout.dto.response;


import lombok.*;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InterestResponseDto {
    private Long id;
    private String name;
    private Boolean status;
    private Long companyId;

}
