package com.sales_scout.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyResponseDto {
    private Long id;
    private Long commentId;
    private String replyTxt;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
}
