package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import lombok.Builder;

import java.util.*;
import java.util.stream.Collectors;

public record ArticleCommentDtos(
        List<ArticleCommentDto> items
) {
    @Builder
    public ArticleCommentDtos {}

    public static ArticleCommentDtos from(Set<ArticleComment> articleComments) {
        Map<Long, List<ArticleCommentDto>> groupedByParentId = articleComments.stream()
                .filter(ArticleComment::hasParentComment)
                .map(ArticleCommentDto::from)
                .collect(Collectors.groupingBy(ArticleCommentDto::parentCommentId));

        List<ArticleCommentDto> rootComments = articleComments.stream()
                .filter(ArticleComment::isRootComment)
                .map(ArticleCommentDto::from)
                .sorted(Comparator.comparing(ArticleCommentDto::createdAt).reversed())
                .toList();

        rootComments.forEach(comment -> comment.setChildren(groupedByParentId));

        return ArticleCommentDtos.builder()
                .items(rootComments)
                .build();
    }
}
