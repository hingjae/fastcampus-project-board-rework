package com.fastcampus.fastcampusboardrework.article.controller.dto;

import com.fastcampus.fastcampusboardrework.article.controller.dto.response.ArticleCommentResponse;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleCommentDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.UserAccountDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    @DisplayName("ArticleCommentDto에서 ArticleCommentResponse로 변환한다. 모든 댓글을 시간순으로 정렬한다.")
    @Test
    public void toArticleCommentResponseWithChildren() {
        ArticleCommentDto comment2 = getArticleCommentDto(2L, "comment2", LocalDateTime.of(2024, 7, 17, 0, 20, 0), null);
        ArticleCommentDto comment3 = getArticleCommentDto(3L, "comment3", LocalDateTime.of(2024, 7, 17, 0, 30, 0), null);
        ArticleCommentDto comment4 = getArticleCommentDto(4L, "comment4", LocalDateTime.of(2024, 7, 17, 0, 40, 0), null);
        ArticleCommentDto comment5 = getArticleCommentDto(5L, "comment5", LocalDateTime.of(2024, 7, 17, 0, 50, 0), null);
        ArticleCommentDto comment1 = getArticleCommentDto(1L, "comment1", LocalDateTime.of(2024, 7, 17, 0, 10, 0), List.of(comment4, comment2, comment5, comment3));

        ArticleCommentResponse response = ArticleCommentResponse.from(comment1);

        assertThat(response.id()).isEqualTo(comment1.id());
        assertThat(response.content()).isEqualTo(comment1.content());
        assertThat(response.childComments()).hasSize(4)
                .extracting(ArticleCommentResponse::content)
                .containsExactly("comment5", "comment4", "comment3", "comment2");
    }

    private ArticleCommentDto getArticleCommentDto(Long id, String content, LocalDateTime createdAt, List<ArticleCommentDto> children) {
        return ArticleCommentDto.builder()
                .id(id)
                .userAccountDto(getUserAccountDto())
                .content(content)
                .createdAt(createdAt)
                .childComments(children)
                .build();
    }

    private ArticleCommentDto getArticleCommentDto() {
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
                .userId("foo userId")
                .nickname("foo nickname")
                .build();
    }
}