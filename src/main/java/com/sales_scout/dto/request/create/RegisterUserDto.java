package com.sales_scout.dto.request.create;

import com.sales_scout.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto extends BaseDto {

        private String email;

        private String password;

        private String fullName;

}
