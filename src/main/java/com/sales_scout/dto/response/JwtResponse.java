package com.sales_scout.dto.response;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class JwtResponse {
    public String token;
    public UserDetails userResponseDto;
    public UserResponseDto user;

    JwtResponse( String token){
        this.token = token;
    }

    public JwtResponse(String token, UserDetails userResponseDto) {
        this.token = token;
        this.userResponseDto = userResponseDto;
    }
}
