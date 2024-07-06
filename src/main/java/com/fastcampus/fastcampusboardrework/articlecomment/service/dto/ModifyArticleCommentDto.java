package com.fastcampus.fastcampusboardrework.articlecomment.service.dto;

import lombok.Builder;

public record ModifyArticleCommentDto(
        String content
) {
    @Builder
    public ModifyArticleCommentDto {
    }
}
