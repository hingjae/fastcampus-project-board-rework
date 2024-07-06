package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import lombok.Builder;

import java.time.LocalDateTime;

public record ArticleWithCommentsDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        ArticleCommentDtos articleComments
) {
    @Builder
    public ArticleWithCommentsDto {}

    public static ArticleWithCommentsDto from(Article article) {
        return ArticleWithCommentsDto.builder()
                .id(article.getId())
                .userAccountDto(UserAccountDto.from(article.getUserAccount()))
                .title(article.getTitle())
                .content(article.getContent())
                .hashtag(article.getHashtag())
                .createdAt(article.getCreatedAt())
                .articleComments(ArticleCommentDtos.from(article.getArticleComments()))
                .build();
    }
}
