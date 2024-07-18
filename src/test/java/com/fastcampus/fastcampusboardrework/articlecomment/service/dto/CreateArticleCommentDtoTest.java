package com.fastcampus.fastcampusboardrework.articlecomment.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleCommentDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.UserAccountDto;
import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CreateArticleCommentDtoTest {

    @DisplayName("content, Article, UserAccount를 입력으로 ArticleComment를 생성할 수 있다.")
    @Test
    public void ArticleCommentDtoToEntity() {
        UserAccount userAccount = getUserAccount();
        Article article = getArticle(userAccount);

        CreateArticleCommentDto createArticleCommentDto = CreateArticleCommentDto
                .builder()
                .content("foo content")
                .build();

        ArticleComment articleComment = createArticleCommentDto.toEntity(article, userAccount, null);

        assertThat(articleComment.getContent()).isEqualTo("foo content");
        assertThat(articleComment.getArticle()).isEqualTo(article);
        assertThat(articleComment.getUserAccount()).isEqualTo(userAccount);
    }

    @DisplayName("ArticleCommentDto를 ArticleComment 엔티티로 변환할 수 있다.")
    @Test
    public void ArticleCommentDtoToEntity2() {
        ArticleCommentDto articleCommentDto = ArticleCommentDto.builder()
                .id(1L)
                .userAccountDto(getUserAccountDto())
                .parentCommentId(getArticleCommentDto().id())
                .content("world")
                .build();

        assertThat(articleCommentDto).isNotNull();
        assertThat(articleCommentDto.id()).isEqualTo(1L);
        assertThat(articleCommentDto.userAccountDto().userId()).isEqualTo("fooUserId");
        assertThat(articleCommentDto.userAccountDto().email()).isEqualTo("fooEmail");
        assertThat(articleCommentDto.content()).isEqualTo("world");
        assertThat(articleCommentDto.userAccountDto().memo()).isEqualTo("fooMemo");
    }

    private ArticleCommentDto getArticleCommentDto() {
        return ArticleCommentDto.builder()
                .id(1L)
                .userAccountDto(getUserAccountDto())
                .parentCommentId(null)
                .content("hello")
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

    private Article getArticle(UserAccount userAccount) {
        return Article.builder()
                .userAccount(userAccount)
                .title("fooTitle")
                .content("barContent")
                .build();
    }

    private UserAccount getUserAccount() {
        return UserAccount.builder()
                .userId("fooUserId")
                .userPassword("fooPassword")
                .nickname("fooNickname")
                .email("fooEmail")
                .build();
    }
}