package com.fastcampus.fastcampusboardrework.articlecomment.controller.dto.request;

import com.fastcampus.fastcampusboardrework.articlecomment.service.dto.CreateArticleCommentDto;
import lombok.Builder;

public record ArticleCommentRequest(
        Long articleId,
        Long parentCommentId,
        String content
) {
    @Builder
    public ArticleCommentRequest {
    }

    public CreateArticleCommentDto toDto() {
        return CreateArticleCommentDto.builder()
                .articleId(articleId)
                .parentCommentId(parentCommentId)
                .content(content)
                .build();
    }
}
