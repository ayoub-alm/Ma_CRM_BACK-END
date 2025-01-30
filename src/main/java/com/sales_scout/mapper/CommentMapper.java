package com.sales_scout.mapper;

import com.sales_scout.dto.response.CommentResponseDto;
import com.sales_scout.entity.Comment;
import org.springframework.stereotype.Component;



@Component
public class CommentMapper {

    public CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .entity(comment.getEntity())
                .entityId(comment.getEntityId())
                .commentTxt(comment.getCommentTxt())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getName())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
