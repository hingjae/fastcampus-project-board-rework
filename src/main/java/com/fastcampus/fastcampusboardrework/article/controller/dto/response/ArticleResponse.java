package com.fastcampus.fastcampusboardrework.article.controller.dto.response;

import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.HashtagDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

public record ArticleResponse(
        Long id,
        String userId,
        String nickname,
        String title,
        String content,
        Collection<String> hashtags,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String createdBy,
        String modifiedBy
) {
    @Builder
    public ArticleResponse {
    }

    public static ArticleResponse from(ArticleDto articleDto) {
        return ArticleResponse.builder()
                .id(articleDto.id())
                .userId(articleDto.userAccountDto().userId())
                .nickname(articleDto.userAccountDto().nickname())
                .title(articleDto.title())
                .content(articleDto.content())
                .hashtags(
                        articleDto.hashtags().hashtagDtos().stream()
                                .map(HashtagDto::hashtagName)
                                .collect(Collectors.toSet())
                )
                .createdAt(articleDto.createdAt())
                .createdBy(articleDto.createdBy())
                .modifiedAt(articleDto.modifiedAt())
                .modifiedBy(articleDto.modifiedBy())
                .build();
    }
}
