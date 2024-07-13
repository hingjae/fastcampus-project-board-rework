package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.domain.ArticleHashtag;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public record ArticleDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        HashtagDtos hashtags,
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
                .createdBy(article.getCreatedBy())
                .modifiedAt(article.getModifiedAt())
                .modifiedBy(article.getModifiedBy())
                .build();
    }
}
