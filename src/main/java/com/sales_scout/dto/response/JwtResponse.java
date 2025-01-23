package com.sales_scout.dto.response;

import com.sales_scout.dto.BaseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
public class JwtResponse extends BaseDto {
    public String token;
    public UserDetails userResponseDto;

    JwtResponse( String token){
        this.token = token;
    }

    public JwtResponse(String token, UserDetails userResponseDto) {
        this.token = token;
        this.userResponseDto = userResponseDto;
    }
}
