package com.fastcampus.fastcampusboardrework.articlecomment.controller.dto.request;

import com.fastcampus.fastcampusboardrework.articlecomment.service.dto.CreateArticleCommentDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleCommentRequestTest {

    @DisplayName("ArticleCommentRequest를 DTO로 변환한다.")
    @Test
    void createArticleCommentRequest() {
        ArticleCommentRequest request = ArticleCommentRequest.builder()
                .articleId(1L)
                .parentCommentId(2L)
                .content("hello")
                .build();

        CreateArticleCommentDto dto = request.toDto();

        assertThat(dto.articleId()).isEqualTo(request.articleId());
        assertThat(dto.parentCommentId()).isEqualTo(request.parentCommentId());
        assertThat(dto.content()).isEqualTo(request.content());
    }
}