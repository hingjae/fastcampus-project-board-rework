package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import lombok.Builder;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleCommentDtos(
        Set<ArticleCommentDto> items
) {
    @Builder
    public ArticleCommentDtos {}

    public static ArticleCommentDtos from(Set<ArticleComment> articleComments) {
        return ArticleCommentDtos.builder()
                .items(articleComments.stream()
                        .map(ArticleCommentDto::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)))
                .build();
    }
}
