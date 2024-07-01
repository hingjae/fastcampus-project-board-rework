package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import lombok.Builder;

public record ArticleModifyDto(
        String title,
        String content,
        String hashtag
) {
    @Builder
    public ArticleModifyDto {
    }
}
