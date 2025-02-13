package com.sales_scout.dto.request;

import com.sales_scout.entity.Right;
import com.sales_scout.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRequestDto {
        private String name;
        private String email;
        private String password;
        private String aboutMe;
        private String phone;
        private Role role;
        private String logo;
        private String matriculate;
        private List<Right> rights;
}
