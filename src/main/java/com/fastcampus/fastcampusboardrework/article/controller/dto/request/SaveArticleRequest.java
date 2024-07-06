package com.fastcampus.fastcampusboardrework.article.controller.dto.request;

import com.fastcampus.fastcampusboardrework.article.service.dto.CreateArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ModifyArticleDto;
import lombok.Builder;

public record SaveArticleRequest(
        String title,
        String content,
        String hashtag
) {
    @Builder
    public SaveArticleRequest {
    }

    public CreateArticleDto toCreateDto() {
        return CreateArticleDto.builder()
                .title(title)
                .content(content)
                .hashtag(hashtag)
                .build();
    }

    public ModifyArticleDto toModifyDto() {
        return ModifyArticleDto.builder()
                .title(title)
                .content(content)
                .hashtag(hashtag)
                .build();
    }
}
