package com.sales_scout.dto.request.create;


import com.sales_scout.dto.BaseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class JwtRequest extends BaseDto {
    private String email;
    private String password;



}
