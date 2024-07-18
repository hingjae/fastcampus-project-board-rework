package com.fastcampus.fastcampusboardrework.article.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ArticleCommentDtoTest {

    @DisplayName("groupedByParentId로 대댓글을 넣는다. 최신 순으로 정렬한다.")
    @Test
    public void setChildren() {
        ArticleCommentDto root1 = getComment(1L, null, "root1", LocalDateTime.of(2024, 7, 18, 0, 0, 0));
        ArticleCommentDto children1 = getComment(2L, 1L, "children1", LocalDateTime.of(2024, 7, 18, 1, 0, 0));
        ArticleCommentDto children2 = getComment(3L, 1L, "children2", LocalDateTime.of(2024, 7, 18, 2, 0, 0));
        ArticleCommentDto root2 = getComment(4L, null, "root2", LocalDateTime.of(2024, 7, 18, 1, 0, 0));
        ArticleCommentDto children3 = getComment(5L, 4L, "children3", LocalDateTime.of(2024, 7, 18, 3, 0, 0));
        ArticleCommentDto children4 = getComment(6L, 4L, "children4", LocalDateTime.of(2024, 7, 18, 2, 0, 0));
        Map<Long, List<ArticleCommentDto>> groupedByParentId = Map.of(1L, List.of(children1, children2), 4L, List.of(children3, children4));

        root1.setChildren(groupedByParentId);
        root2.setChildren(groupedByParentId);

        List<ArticleCommentDto> result1 = root1.childComments();
        List<ArticleCommentDto> result2 = root2.childComments();
        assertThat(result1).hasSize(2)
                .extracting(ArticleCommentDto::content)
                .containsExactly("children1", "children2");
        assertThat(result2).hasSize(2)
                .extracting(ArticleCommentDto::content)
                .containsExactly("children4", "children3");
    }

    private ArticleCommentDto getComment(Long id, Long parentCommentId, String content, LocalDateTime createdAt) {
        return ArticleCommentDto.builder()
                .id(id)
                .userAccountDto(getUserAccountDto())
                .parentCommentId(parentCommentId)
                .content(content)
                .createdAt(createdAt)
                .childComments(new ArrayList<>())
                .build();
    }

    private UserAccountDto getUserAccountDto() {
        return UserAccountDto.builder()
                .userId("fooUserId")
                .userPassword("fooPw")
                .email("fooEmail")
                .nickname("fooNickname")
                .memo("fooMemo")
                .build();
    }
}