package com.fastcampus.fastcampusboardrework.article.service.dto;

import lombok.Builder;

import java.util.Set;

public record HashtagDtos(
        Set<HashtagDto> hashtagDtos
) {
    @Builder
    public HashtagDtos {
    }

    public static HashtagDtos from(Set<HashtagDto> hashtagDtos) {
        return HashtagDtos.builder()
                .hashtagDtos(hashtagDtos)
                .build();
    }
}
