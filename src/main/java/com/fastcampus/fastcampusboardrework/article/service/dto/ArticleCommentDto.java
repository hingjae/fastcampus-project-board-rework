package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public record ArticleCommentDto(
        Long id,
        UserAccountDto userAccountDto,
        Long parentCommentId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        List<ArticleCommentDto> childComments
) {
    @Builder
    public ArticleCommentDto {
    }

    public static ArticleCommentDto from(ArticleComment articleComment) {
        return ArticleCommentDto.builder()
                .id(articleComment.getId())
                .userAccountDto(UserAccountDto.from(articleComment.getUserAccount()))
                .parentCommentId(articleComment.getParentCommentId())
                .content(articleComment.getContent())
                .createdAt(articleComment.getCreatedAt())
                .modifiedAt(articleComment.getModifiedAt())
                .childComments(new ArrayList<>())
                .build();
    }

    public ArticleComment toEntity(ArticleComment parentComment) {
        return ArticleComment.builder()
                .id(id)
                .userAccount(userAccountDto.toEntity())
                .parentComment(parentComment)
                .content(content)
                .build();
    }


    public void setChildren(Map<Long, List<ArticleCommentDto>> groupedByParentId) {
        List<ArticleCommentDto> childComments = Optional.ofNullable(groupedByParentId.get(this.id))
                .orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(ArticleCommentDto::createdAt))
                .toList();

        childComments.forEach(child -> child.setChildren(groupedByParentId));
        this.childComments.addAll(childComments);
    }
}
