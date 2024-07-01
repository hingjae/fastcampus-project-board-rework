package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import lombok.Builder;

public record ArticleCreateDto(
        UserAccountDto userAccountDto,
        String title,
        String content,
        String hashtag
) {
    @Builder
    public ArticleCreateDto(UserAccountDto userAccountDto, String title, String content, String hashtag) {
        this.userAccountDto = userAccountDto;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public Article toEntity() {
        return Article.builder()
                .userAccount(userAccountDto.toEntity())
                .title(title)
                .content(content)
                .hashtag(hashtag)
                .build();
    }
}
