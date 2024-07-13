package com.fastcampus.fastcampusboardrework.article.controller.dto;

import com.fastcampus.fastcampusboardrework.article.controller.dto.response.ArticleWithCommentsResponse;
import com.fastcampus.fastcampusboardrework.article.service.dto.*;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleWithCommentsResponseTest {

    @DisplayName("ArticleWithCommentDto에서 ArticleWithCommentResponse로 변환한다.")
    @Test
    public void toArticleWithCommentResponse() {
        ArticleWithCommentsDto dto = getArticleWithCommentsDto();

        ArticleWithCommentsResponse response = ArticleWithCommentsResponse.from(dto);

        assertThat(response.id()).isEqualTo(dto.id());
        assertThat(response.userId()).isEqualTo(dto.userAccountDto().userId());
        assertThat(response.nickname()).isEqualTo(dto.userAccountDto().nickname());
        assertThat(response.title()).isEqualTo(dto.title());
        assertThat(response.content()).isEqualTo(dto.content());
        assertThat(response.articleComments()).hasSize(1)
                .extracting("id", "userId", "nickname", "content")
                .containsExactly(
                        Tuple.tuple(
                                getArticleComment().id(),
                                getArticleComment().userAccountDto().userId(),
                                getArticleComment().userAccountDto().nickname(),
                                getArticleComment().content())
                );
    }

    private ArticleWithCommentsDto getArticleWithCommentsDto() {
        return ArticleWithCommentsDto.builder()
                .id(1L)
                .userAccountDto(getUserAccountDto())
                .title("foo title")
                .content("foo content")
                .articleComments(getArticleCommentDtos())
                .hashtags(getHashtagDtos())
                .build();
    }

    private HashtagDtos getHashtagDtos() {
        return HashtagDtos.from(Set.of(getHashtagDto(1L, "foo"), getHashtagDto(2L, "bar")));
    }

    private HashtagDto getHashtagDto(Long id, String hashtagName) {
        return HashtagDto.builder()
                .id(id)
                .hashtagName(hashtagName)
                .build();
    }

    private ArticleCommentDtos getArticleCommentDtos() {
        return ArticleCommentDtos.builder()
                .items(getArticleComments())
                .build();
    }

    private Set<ArticleCommentDto> getArticleComments() {
        return Set.of(
                getArticleComment()
        );
    }

    private ArticleCommentDto getArticleComment() {
        return ArticleCommentDto.builder()
                .id(1L)
                .content("comment")
                .userAccountDto(getUserAccountDto())
                .build();
    }

    private UserAccountDto getUserAccountDto() {
        return UserAccountDto.builder()
                .userId("foouserId")
                .nickname("foonickname")
                .build();
    }

}