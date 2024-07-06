package com.fastcampus.fastcampusboardrework.article.service.dto;

import lombok.Builder;

public record ModifyArticleDto(
        String title,
        String content,
        String hashtag
) {
    @Builder
    public ModifyArticleDto {
    }
}
