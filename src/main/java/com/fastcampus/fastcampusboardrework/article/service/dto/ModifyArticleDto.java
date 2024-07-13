package com.fastcampus.fastcampusboardrework.article.service.dto;

import lombok.Builder;

import java.util.List;

public record ModifyArticleDto(
        String title,
        String content,
        String hashtagNames
) {
    @Builder
    public ModifyArticleDto {
    }
}
