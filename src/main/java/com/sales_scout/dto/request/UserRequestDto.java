package com.sales_scout.dto.request;

import com.sales_scout.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto extends BaseDto {
        private String name;
        private String email;
        private String password;
        private String aboutMe;
}
