package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import lombok.Builder;

public record CreateArticleDto(
        UserAccountDto userAccountDto,
        String title,
        String content,
        String hashtag
) {
    @Builder
    public CreateArticleDto {
    }

    public Article toEntity() {
        return Article.builder()
                .userAccount(userAccountDto.toEntity())
                .title(title)
                .content(content)
                .hashtag(hashtag)
                .build();
    }
}
