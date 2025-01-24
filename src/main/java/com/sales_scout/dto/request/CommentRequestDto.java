package com.sales_scout.dto.request;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.enums.EntityEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CommentRequestDto extends BaseDto {
        private  EntityEnum entity;
        private String commentTxt;
        private  Long userId;
        private  Long entityId;
}
