package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleCommentDtosTest {

    @DisplayName("articleComments를 입력하면 계층구조의 ArticleCommentDto를 반환한다. ")
    @Test
    public void ArticleCommentDtosTest() {
        ArticleComment root1 = getComment(1L, null, "root1", LocalDateTime.of(2024, 7, 18, 0, 0, 0));
        ArticleComment children1 = getComment(2L, root1, "children1", LocalDateTime.of(2024, 7, 18, 1, 0, 0));
        ArticleComment children2 = getComment(3L, root1, "children2", LocalDateTime.of(2024, 7, 18, 2, 0, 0));
        ArticleComment root2 = getComment(4L, null, "root2", LocalDateTime.of(2024, 7, 18, 1, 0, 0));
        ArticleComment children3 = getComment(5L, root2, "children3", LocalDateTime.of(2024, 7, 18, 3, 0, 0));
        ArticleComment children4 = getComment(6L, root2, "children4", LocalDateTime.of(2024, 7, 18, 2, 0, 0));

        List<ArticleCommentDto> result = ArticleCommentDtos.from(Set.of(root1, children1, children2, children3, children4, root2)).items();

        assertThat(result).hasSize(2)
                .extracting(ArticleCommentDto::content)
                .containsExactly("root2", "root1");

        assertThat(result.get(0).childComments()).hasSize(2)
                .extracting(ArticleCommentDto::content)
                .containsExactly("children4", "children3");

        assertThat(result.get(1).childComments()).hasSize(2)
                .extracting(ArticleCommentDto::content)
                .containsExactly("children1", "children2");
    }

    private ArticleComment getComment(Long id, ArticleComment parentComment, String content, LocalDateTime createdAt) {
        return ArticleComment.builder()
                .id(id)
                .userAccount(getUserAccountDto())
                .parentComment(parentComment)
                .content(content)
                .createdAt(createdAt)
                .build();
    }

    private UserAccount getUserAccountDto() {
        return UserAccount.builder()
                .userId("fooUserId")
                .userPassword("fooPw")
                .email("fooEmail")
                .nickname("fooNickname")
                .memo("fooMemo")
                .build();
    }
}