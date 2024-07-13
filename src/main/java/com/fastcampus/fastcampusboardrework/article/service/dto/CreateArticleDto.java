package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import lombok.Builder;

public record CreateArticleDto(
        String title,
        String content,
        String hashtagNames
) {
    @Builder
    public CreateArticleDto {
    }

    public Article toEntity(UserAccount userAccount) {
        return Article.create(userAccount, title, content);
    }
}
