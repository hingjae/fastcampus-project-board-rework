package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import lombok.Builder;

import java.time.LocalDateTime;

public record ArticleCommentDto(
        Long id,
        UserAccountDto userAccountDto,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    @Builder
    public ArticleCommentDto {
    }

    public static ArticleCommentDto from(ArticleComment articleComment) {
        return ArticleCommentDto.builder()
                .id(articleComment.getId())
                .userAccountDto(UserAccountDto.from(articleComment.getUserAccount()))
                .content(articleComment.getContent())
                .createdAt(articleComment.getCreatedAt())
                .modifiedAt(articleComment.getModifiedAt())
                .build();
    }
}
