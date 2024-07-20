package com.fastcampus.fastcampusboardrework.article.controller.dto.response;

import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleWithCommentsDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentsResponse(
        Long id,
        String userId,
        String email,
        String nickname,
        String title,
        String content,
        Set<HashtagResponse> hashtags,
        LocalDateTime createdAt,
        List<ArticleCommentResponse> articleComments
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
                .hashtags(
                        articleWithComments.hashtags().hashtagDtos().stream()
                                .map(HashtagResponse::from)
                                .collect(Collectors.toSet())
                )
                .createdAt(articleWithComments.createdAt())
                .articleComments(
                        articleWithComments.articleComments().items()
                            .stream()
                            .map(ArticleCommentResponse::from)
                            .sorted(Comparator.comparing(ArticleCommentResponse::createdAt).reversed())
                            .collect(Collectors.toCollection(ArrayList::new))
                )
                .build();
    }
}
