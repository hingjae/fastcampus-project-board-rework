package com.fastcampus.fastcampusboardrework.article.controller.dto.response;

import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleCommentDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public record ArticleCommentResponse(
        Long id,
        String content,
        String nickname,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String userId,
        Long parentCommentId,
        List<ArticleCommentResponse> childComments
) {
    @Builder
    public ArticleCommentResponse {
    }

    public static ArticleCommentResponse from(ArticleCommentDto articleCommentDto) {
        return ArticleCommentResponse.builder()
                .id(articleCommentDto.id())
                .content(articleCommentDto.content())
                .nickname(articleCommentDto.userAccountDto().nickname())
                .createdAt(articleCommentDto.createdAt())
                .modifiedAt(articleCommentDto.modifiedAt())
                .userId(articleCommentDto.userAccountDto().userId())
                .parentCommentId(articleCommentDto.parentCommentId())
                .childComments(
                        Optional.ofNullable(articleCommentDto.childComments())
                                .map(childComments -> childComments.stream()
                                        .sorted(Comparator.comparing(ArticleCommentDto::createdAt).reversed())
                                        .map(ArticleCommentResponse::from)
                                        .collect(Collectors.toCollection(ArrayList::new)))
                                .orElse(null)
                )
                .build();
    }

}
