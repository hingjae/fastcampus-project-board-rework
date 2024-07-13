package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import lombok.Builder;

public record HashtagDto(
        Long id,
        String hashtagName
) {
    @Builder
    public HashtagDto {
    }

    public static HashtagDto from(Hashtag hashtag) {
        return HashtagDto.builder()
                .id(hashtag.getId())
                .hashtagName(hashtag.getHashtagName())
                .build();
    }
}
