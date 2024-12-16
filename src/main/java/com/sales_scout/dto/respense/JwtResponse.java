package com.sales_scout.dto.respense;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
public class JwtResponse {
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
