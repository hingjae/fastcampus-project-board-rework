package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import lombok.Builder;

public record ArticleCommentDto(
        Long id,
        UserAccountDto userAccount,
        String content
) {
    @Builder
    public ArticleCommentDto {
    }

    public static ArticleCommentDto from(ArticleComment articleComment) {
        return ArticleCommentDto.builder()
                .id(articleComment.getId())
                .userAccount(UserAccountDto.from(articleComment.getUserAccount()))
                .content(articleComment.getContent())
                .build();
    }
}
