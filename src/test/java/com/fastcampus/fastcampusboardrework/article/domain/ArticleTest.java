package com.fastcampus.fastcampusboardrework.article.domain;

import com.fastcampus.fastcampusboardrework.article.domain.exception.UserNotAuthorizedException;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ArticleTest {

    @DisplayName("변경 내용을 입력해서 Article을 수정한다.")
    @Test
    public void modifyArticle() {
        Article article = Article.builder()
                .id(1L)
                .title("article title")
                .content("article content")
                .hashtag("article hashtag")
                .build();
        String newTitle = "newTitle";
        String newContent = "newContent";
        String newHashtag = "newHashtag";

        article.modify(newTitle, newContent, newHashtag);

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
        assertThat(article.getHashtag()).isEqualTo(newHashtag);
    }

    @DisplayName("게시판과 유저가 서로 연관관계이다.")
    @Test
    public void articleBelongsToUserAccount() {
        UserAccount userAccount1 = getUserAccount("foo1", "fooEmail1");
        Article article = getArticle(userAccount1);

        assertThatCode(() -> article.validateUserAccount(userAccount1))
                .doesNotThrowAnyException();
    }

    @DisplayName("게시판과 유저가 서로 연관관계가 아니다.")
    @Test
    public void articleNotBelongsToUserAccount() {
        UserAccount userAccount1 = getUserAccount("foo1", "fooEmail1");
        UserAccount userAccount2 = getUserAccount("foo2", "fooEmail2");
        Article article = getArticle(userAccount1);

        assertThatThrownBy(() -> article.validateUserAccount(userAccount2))
                .isInstanceOf(UserNotAuthorizedException.class)
                .hasMessage("User Not Authorized");;
    }

    private Article getArticle(UserAccount userAccount) {
        return Article.builder()
                .userAccount(userAccount)
                .title("test title")
                .content("test content")
                .hashtag("test tag")
                .build();
    }

    private UserAccount getUserAccount(String userId, String email) {
        return UserAccount.builder()
                .userId(userId)
                .userPassword("pw")
                .email(email)
                .nickname("nickname")
                .memo("memo")
                .build();
    }
}