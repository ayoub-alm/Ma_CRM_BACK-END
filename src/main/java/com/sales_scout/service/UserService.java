package com.sales_scout.service;


import com.sales_scout.dto.request.UserRequestDto;
import com.sales_scout.dto.request.create.UserRightsRequestDto;
import com.sales_scout.dto.response.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserResponseDto> getAllUser();
    public UserResponseDto createUser(UserRequestDto userRequestDto);
    public UserResponseDto findById(Long id);
    UserResponseDto findByEmail(String email);
    Boolean softDeleteUser(Long id);
    Boolean restoreUser(Long id);
    public UserResponseDto updateUser(Long id,UserRequestDto userRequestDto);
    UserResponseDto updateUserRole(Long Id, UserRequestDto userRequestDto);
    UserResponseDto addRightsToUser(UserRightsRequestDto userRightsRequestDto);
    UserResponseDto removeRightsFromUser(UserRightsRequestDto userRightsRequestDto);
}
