package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleDto(
        Long id,
        UserAccountDto userAccount,
        String title,
        String content,
        String hashtag,
        Set<ArticleCommentDto> articleComments,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String createdBy,
        String modifiedBy
) {
    @Builder
    public ArticleDto {
    }

    public static ArticleDto from(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .userAccount(UserAccountDto.from(article.getUserAccount()))
                .title(article.getTitle())
                .content(article.getContent())
                .hashtag(article.getHashtag())
                .articleComments(
                        article.getArticleComments().stream()
                                .map(ArticleCommentDto::from)
                                .collect(Collectors.toCollection(LinkedHashSet::new))
                )
                .createdAt(article.getCreatedAt())
                .createdBy(article.getCreatedBy())
                .modifiedAt(article.getModifiedAt())
                .modifiedBy(article.getModifiedBy())
                .build();
    }
}
