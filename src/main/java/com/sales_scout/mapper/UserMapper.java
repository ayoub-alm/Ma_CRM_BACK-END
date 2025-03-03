package com.sales_scout.mapper;

import com.sales_scout.dto.request.UserRequestDto;
import com.sales_scout.dto.response.RightsResponseDto;
import com.sales_scout.dto.response.UserResponseDto;
import com.sales_scout.entity.Right;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.entity.UserRights;
import com.sales_scout.repository.UserRightsRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final UserRightsRepository userRightsRepository;

    public UserMapper(UserRightsRepository userRightsRepository) {
        this.userRightsRepository = userRightsRepository;
    }


    public  UserResponseDto fromEntity(UserEntity userEntity) {
        List<UserRights> userRights = userRightsRepository.findByUserId(userEntity.getId());

        List<RightsResponseDto> rightsResponseDtos = userRights.stream()
                .map(userRights1 -> new RightsResponseDto(
                        userRights1.getRight().getId(),
                        userRights1.getRight().getName(),
                        userRights1.getRight().getDescription(),
                        userRights1.getRight().getCompany().getId()
                ))
                .collect(Collectors.toList());

        return UserResponseDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .role(userEntity.getRole())
                .matriculate(userEntity.getMatriculate())
                .logo(userEntity.getLogo())
                .rights(rightsResponseDtos)
                .build();
    }

    public  UserEntity fromDto(UserRequestDto userRequestDto){
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
