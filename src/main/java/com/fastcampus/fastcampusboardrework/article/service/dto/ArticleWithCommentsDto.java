package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import lombok.Builder;

public record ArticleWithCommentsDto(
        Long id,
        UserAccountDto userAccount,
        String title,
        String content,
        String hashtag,
        ArticleCommentDtos articleComments
) {
    @Builder
    public ArticleWithCommentsDto {}

    public static ArticleWithCommentsDto from(Article article) {
        return ArticleWithCommentsDto.builder()
                .id(article.getId())
                .userAccount(UserAccountDto.from(article.getUserAccount()))
                .title(article.getTitle())
                .content(article.getContent())
                .hashtag(article.getHashtag())
                .articleComments(ArticleCommentDtos.from(article.getArticleComments()))
                .build();
    }
}
