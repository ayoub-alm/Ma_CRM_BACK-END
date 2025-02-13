package com.sales_scout.mapper;

import com.sales_scout.dto.request.UserRequestDto;
import com.sales_scout.dto.response.RightsResponseDto;
import com.sales_scout.dto.response.UserResponseDto;
import com.sales_scout.entity.Right;
import com.sales_scout.entity.UserEntity;
import java.util.stream.Collectors;

public class UserMapper {


    public static UserResponseDto fromEntity(UserEntity userEntity){
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .role(userEntity.getRole())
                .matriculate(userEntity.getMatriculate())
                .logo(userEntity.getLogo())
                .rights(userEntity.getUserRights() != null ? userEntity.getUserRights()
                        .stream()
                        .map(userRights->
                        {
                            Right right = userRights.getRight();
                            return new RightsResponseDto(right.getId(), right.getName(), right.getDescription(), right.getCompany() != null ? right.getCompany().getId() : null);
                        }).collect(Collectors.toList()) : null)
                .build();
    }

    public static UserEntity fromDto(UserRequestDto userRequestDto){
        return UserEntity.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .phone(userRequestDto.getPhone())
                .role(userRequestDto.getRole())
                .matriculate(userRequestDto.getMatriculate())
                .logo(userRequestDto.getLogo())
                .build();
    }
}
