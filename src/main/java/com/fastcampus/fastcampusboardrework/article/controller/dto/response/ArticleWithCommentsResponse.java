package com.fastcampus.fastcampusboardrework.article.controller.dto.response;

import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleWithCommentsDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentsResponse(
        Long id,
        String userId,
        String email,
        String nickname,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        Set<ArticleCommentResponse> articleComments
) {
    @Builder
    public ArticleWithCommentsResponse {
    }

    public static ArticleWithCommentsResponse from(ArticleWithCommentsDto articleWithComments) {
        return ArticleWithCommentsResponse.builder()
                .id(articleWithComments.id())
                .userId(articleWithComments.userAccountDto().userId())
                .email(articleWithComments.userAccountDto().email())
                .nickname(articleWithComments.userAccountDto().nickname())
                .title(articleWithComments.title())
                .content(articleWithComments.content())
                .hashtag(articleWithComments.hashtag())
                .createdAt(articleWithComments.createdAt())
                .articleComments(articleWithComments.articleComments().items()
                        .stream()
                        .map(ArticleCommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)))
                .build();
    }
}
