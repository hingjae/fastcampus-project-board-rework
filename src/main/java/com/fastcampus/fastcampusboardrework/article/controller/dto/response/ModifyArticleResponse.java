package com.fastcampus.fastcampusboardrework.article.controller.dto.response;

import com.fastcampus.fastcampusboardrework.article.service.dto.ModifyArticleDto;
import lombok.Builder;

public record ModifyArticleResponse(
        Long id,
        String title,
        String content
) {
    @Builder
    public ModifyArticleResponse {
    }

    public static ModifyArticleResponse from(ModifyArticleDto dto) {
        return ModifyArticleResponse.builder()
                .id(dto.id())
                .title(dto.title())
                .content(dto.content())
                .build();
    }
}
