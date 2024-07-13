package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.domain.ArticleHashtag;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public record ArticleWithCommentsDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        HashtagDtos hashtags,
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
                .hashtags(
                        HashtagDtos.from(article.getArticleHashtags().stream()
                                .map(ArticleHashtag::getHashtag)
                                .map(HashtagDto::from)
                                .collect(Collectors.toSet()))
                )
                .createdAt(article.getCreatedAt())
                .articleComments(ArticleCommentDtos.from(article.getArticleComments()))
                .build();
    }
}
