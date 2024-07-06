package com.fastcampus.fastcampusboardrework.article.controller.dto;

import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleCommentDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.UserAccountDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleCommentResponseTest {

    @DisplayName("ArticleCommentDto에서 ArticleCommentResponse로 변환한다.")
    @Test
    public void toArticleCommentResponse() {
        ArticleCommentDto dto = getArticleCommentDto();

        ArticleCommentResponse response = ArticleCommentResponse.from(dto);

        assertThat(response.id()).isEqualTo(dto.id());
        assertThat(response.content()).isEqualTo(dto.content());
        assertThat(response.userId()).isEqualTo(dto.userAccountDto().userId());
        assertThat(response.nickname()).isEqualTo(dto.userAccountDto().nickname());
        assertThat(response.createdAt()).isEqualTo(dto.createdAt());
        assertThat(response.modifiedAt()).isEqualTo(dto.modifiedAt());
    }

    private static ArticleCommentDto getArticleCommentDto() {
        return ArticleCommentDto.builder()
                .id(1L)
                .userAccountDto(getUserAccountDto())
                .content("foo content")
                .createdAt(LocalDateTime.of(2024, 7, 6, 0, 0, 0))
                .modifiedAt(LocalDateTime.of(2024, 7, 6, 0, 0, 0))
                .build();
    }

    private static UserAccountDto getUserAccountDto() {
        return UserAccountDto.builder()
                .id(1L)
                .userId("foo userId")
                .nickname("foo nickname")
                .build();
    }
}