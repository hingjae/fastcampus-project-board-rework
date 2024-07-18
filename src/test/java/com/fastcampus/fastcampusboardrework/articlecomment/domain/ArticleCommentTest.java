package com.fastcampus.fastcampusboardrework.articlecomment.domain;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.domain.exception.UserNotAuthorizedException;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ArticleCommentTest {

    @DisplayName("변경 내용을 입력해서 ArticleComment을 수정한다.")
    @Test
    public void modifyArticleCommentTest() {
        UserAccount userAccount = getUserAccount("fooUserId", "fooEmail");
        Article article = getArticle(userAccount);
        ArticleComment articleComment = getArticleComment(userAccount, article);
        String newContent = "new content";

        articleComment.modify(newContent);

        assertThat(articleComment.getContent()).isEqualTo(newContent);
    }

    @DisplayName("댓글과 유저가 서로 연관관계이다.")
    @Test
    public void articleBelongsToUserAccount() {
        UserAccount userAccount1 = getUserAccount("foo1", "fooEmail1");
        Article article = getArticle(userAccount1);
        ArticleComment articleComment = getArticleComment(userAccount1, article);

        assertThatCode(() -> articleComment.validateUserAccount(userAccount1))
                .doesNotThrowAnyException();
    }

    @DisplayName("댓글과 유저가 서로 연관관계가 아니다.")
    @Test
    public void articleNotBelongsToUserAccount() {
        UserAccount userAccount1 = getUserAccount("foo1", "fooEmail1");
        UserAccount userAccount2 = getUserAccount("foo2", "fooEmail2");
        Article article = getArticle(userAccount1);
        ArticleComment articleComment = getArticleComment(userAccount1, article);

        assertThatThrownBy(() -> articleComment.validateUserAccount(userAccount2))
                .isInstanceOf(UserNotAuthorizedException.class)
                .hasMessage("User Not Authorized");;
    }

    @DisplayName("부모 댓글이 있으면 getParentCommentId는 부모댓글Id를 반환한다.")
    @Test
    public void getParentCommentIdTest() {
        UserAccount userAccount = getUserAccount("foo1", "fooEmail1");
        Article article = getArticle(userAccount);
        ArticleComment parentComment = getArticleComment(userAccount, article);
        ArticleComment childComment = getArticleCommentWithParentComment(userAccount, article, parentComment);

        Long parentCommentId = childComment.getParentCommentId();

        assertThat(parentCommentId).isEqualTo(parentComment.getId());
    }

    @DisplayName("부모 댓글이 없으면 getParentCommentId는 null을 반환한다.")
    @Test
    public void getParentCommentIdNullTest() {
        UserAccount userAccount = getUserAccount("foo1", "fooEmail1");
        Article article = getArticle(userAccount);
        ArticleComment comment = getArticleComment(userAccount, article);

        assertThat(comment.getParentCommentId()).isNull();
    }

    private ArticleComment getArticleCommentWithParentComment(UserAccount userAccount, Article article, ArticleComment parentComment) {
        return ArticleComment.builder()
                .userAccount(userAccount)
                .article(article)
                .parentComment(parentComment)
                .content("foo comment")
                .build();
    }

    private ArticleComment getArticleComment(UserAccount userAccount, Article article) {
        return ArticleComment.builder()
                .id(1L)
                .userAccount(userAccount)
                .article(article)
                .content("foo comment")
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

    private Article getArticle(UserAccount userAccount) {
        return Article.builder()
                .userAccount(userAccount)
                .title("test title")
                .content("test content")
                .build();
    }
}