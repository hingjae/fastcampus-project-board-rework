package com.fastcampus.fastcampusboardrework.article.controller.dto;

import com.fastcampus.fastcampusboardrework.article.controller.dto.response.ArticleResponse;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.UserAccountDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleResponseTest {

    @DisplayName("ArticleDto에서 ArticleResponse로 변환한다.")
    @Test
    public void toArticleResponse() {
        ArticleDto dto = getArticleDto();

        ArticleResponse response = ArticleResponse.from(dto);

        assertThat(response.id()).isEqualTo(dto.id());
        assertThat(response.userId()).isEqualTo(dto.userAccountDto().userId());
        assertThat(response.nickname()).isEqualTo(dto.userAccountDto().nickname());
        assertThat(response.title()).isEqualTo(dto.title());
        assertThat(response.content()).isEqualTo(dto.content());
        assertThat(response.hashtag()).isEqualTo(dto.hashtag());
        assertThat(response.createdAt()).isEqualTo(dto.createdAt());
        assertThat(response.createdBy()).isEqualTo(dto.createdBy());
        assertThat(response.modifiedAt()).isEqualTo(dto.modifiedAt());
        assertThat(response.modifiedBy()).isEqualTo(dto.modifiedBy());
    }

    private ArticleDto getArticleDto() {
        return ArticleDto.builder()
                .id(1L)
                .userAccountDto(getUserAccountDto())
                .title("foo title")
                .content("foo content")
                .hashtag("foo hashtag")
                .createdAt(LocalDateTime.of(2024, 7, 6, 0, 0, 0))
                .createdBy("foo createdBy")
                .modifiedAt(LocalDateTime.of(2024, 7, 6, 0, 0, 0))
                .modifiedBy("foo modifiedBy")
                .build();
    }

    private UserAccountDto getUserAccountDto() {
        return UserAccountDto.builder()
                .userId("foouserId")
                .nickname("foonickname")
                .build();
    }
}