package com.sales_scout.dto.response;
import com.sales_scout.dto.BaseDto;
import com.sales_scout.entity.Right;
import com.sales_scout.entity.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto  {
    private String name;
    private String email;
    private String aboutMe;
    private String phone;
    private String logo;
    private String matriculate;
    private long  id;
    private Role role;
    private List<RightsResponseDto> rights;
}
