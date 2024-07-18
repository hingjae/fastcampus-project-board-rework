package com.fastcampus.fastcampusboardrework.articlecomment.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import lombok.Builder;

public record CreateArticleCommentDto(
        Long articleId,
        Long parentCommentId,
        String content
) {
    @Builder
    public CreateArticleCommentDto {
    }

    public ArticleComment toEntity(Article article, UserAccount userAccount, ArticleComment parentComment) {
        return ArticleComment.builder()
                .article(article)
                .userAccount(userAccount)
                .parentComment(parentComment)
                .content(content)
                .build();
    }

    public boolean hasParent() {
        return parentCommentId != null;
    }
}
