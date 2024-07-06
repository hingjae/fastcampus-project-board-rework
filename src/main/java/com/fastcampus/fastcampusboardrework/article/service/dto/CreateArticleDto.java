package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import lombok.Builder;

public record CreateArticleDto(
        String title,
        String content,
        String hashtag
) {
    @Builder
    public CreateArticleDto {
    }

    public Article toEntity(UserAccount userAccount) {
        return Article.builder()
                .userAccount(userAccount)
                .title(title)
                .content(content)
                .hashtag(hashtag)
                .build();
    }
}
