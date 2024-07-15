package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import lombok.Builder;

public record ModifyArticleDto(
        Long id,
        String title,
        String content
) {
    @Builder
    public ModifyArticleDto {
    }

    public static ModifyArticleDto from(Article article) {
        return ModifyArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .build();
    }
}
