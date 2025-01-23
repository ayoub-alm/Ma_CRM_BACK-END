package com.sales_scout.dto.response;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.enums.EntityEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CommentResponseDto extends BaseDto {
    private Long id;
    private String commentTxt;
    private Long userId; // ID of the user who created the comment.
    private String userName; // Optional: User's name for display purposes.
    private EntityEnum entity; // The module name (e.g., "Invoice", "Customer").
    private Long entityId; // The ID of the associated entity.
}