package com.sales_scout.dto.request.create;


import com.sales_scout.dto.BaseDto;
import lombok.Data;

@Data
public class LoginUserDto extends BaseDto {
    private String email;

    private String password;

    // getters and setters here...
}