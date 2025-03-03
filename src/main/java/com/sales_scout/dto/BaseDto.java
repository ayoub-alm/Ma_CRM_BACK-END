package com.sales_scout.dto;

import com.sales_scout.dto.response.UserResponseDto;
import com.sales_scout.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseDto {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponseDto createdBy;
    private UserResponseDto updatedBy;
}
