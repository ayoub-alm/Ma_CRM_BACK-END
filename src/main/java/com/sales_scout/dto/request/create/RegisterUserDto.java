package com.sales_scout.dto.request.create;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto  {

        private String email;

        private String password;

        private String fullName;

}
