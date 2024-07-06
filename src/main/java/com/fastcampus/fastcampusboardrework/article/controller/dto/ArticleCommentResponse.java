package com.fastcampus.fastcampusboardrework.article.controller.dto;

import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleCommentDto;
import lombok.Builder;

import java.time.LocalDateTime;

public record ArticleCommentResponse(
        Long id,
        String content,
        String userId,
        String nickname,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    @Builder
    public ArticleCommentResponse {
    }

    public static ArticleCommentResponse from(ArticleCommentDto articleCommentDto) {
        return ArticleCommentResponse.builder()
                .id(articleCommentDto.id())
                .content(articleCommentDto.content())
                .userId(articleCommentDto.userAccountDto().userId())
                .nickname(articleCommentDto.userAccountDto().nickname())
                .createdAt(articleCommentDto.createdAt())
                .modifiedAt(articleCommentDto.modifiedAt())
                .build();
    }
}
