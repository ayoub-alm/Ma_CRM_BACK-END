package com.sales_scout.dto.response;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.entity.Right;
import com.sales_scout.entity.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto extends BaseDto {
    private String name;
    private String email;
//    private String password;
    private String aboutMe;
    private String phone;
    private long  id;
    private Role role;
    private List<Right> rights;
}
