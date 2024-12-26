package com.sales_scout.service;


import com.sales_scout.dto.request.UserRequestDto;
import com.sales_scout.dto.response.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserResponseDto> getAllUser();
    public UserResponseDto createUser(UserRequestDto userRequestDto);
    public UserResponseDto findById(Long id);
    UserResponseDto findByEmail(String email);
}
