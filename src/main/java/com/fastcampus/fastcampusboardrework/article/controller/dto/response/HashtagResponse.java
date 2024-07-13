package com.fastcampus.fastcampusboardrework.article.controller.dto.response;

import com.fastcampus.fastcampusboardrework.article.service.dto.HashtagDto;
import lombok.Builder;

public record HashtagResponse(
        Long id,
        String hashtagName
) {
    @Builder
    public HashtagResponse {
    }

    public static HashtagResponse from(HashtagDto dto) {
        return HashtagResponse.builder()
                .id(dto.id())
                .hashtagName(dto.hashtagName())
                .build();
    }
}
