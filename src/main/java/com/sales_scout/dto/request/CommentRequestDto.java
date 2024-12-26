package com.sales_scout.dto.request;

import com.sales_scout.enums.EntityEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto{
        private  EntityEnum entity;
        private String commentTxt;
        private  Long userId;
        private  Long entityId;
}
