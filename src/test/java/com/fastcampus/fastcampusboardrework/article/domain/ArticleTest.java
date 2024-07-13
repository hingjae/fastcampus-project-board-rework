package com.fastcampus.fastcampusboardrework.article.domain;

import com.fastcampus.fastcampusboardrework.article.domain.exception.UserNotAuthorizedException;
import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ArticleTest {

    @DisplayName("변경 내용을 입력해서 Article을 수정한다.")
    @Test
    public void modifyArticle() {
        Article article = Article.builder()
                .id(1L)
                .title("article title")
                .content("article content")
                .build();
        String newTitle = "newTitle";
        String newContent = "newContent";

        article.modify(newTitle, newContent);

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
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

    @DisplayName("Article에 ArticleHashtags를 저장한다.")
    @Test
    public void setArticleHashtags() {
        Hashtag hashtag1 = getHashtag(1L, "hashtag1");
        Hashtag hashtag2 = getHashtag(2L, "hashtag2");
        List<Hashtag> hashtags = List.of(hashtag1, hashtag2);
        List<ArticleHashtag> articleHashtags = getArticleHashtags(hashtags);
        Article article = getArticle(getUserAccount("user1", "email"));

        article.addArticleHashtags(articleHashtags);

        assertThat(article.getArticleHashtags()).hasSize(2)
                .extracting(articleHashtag -> articleHashtag.getArticle().getTitle(), articleHashtag -> articleHashtag.getHashtag().getHashtagName())
                .containsExactlyInAnyOrder(
                        Tuple.tuple("test title", hashtag1.getHashtagName()),
                        Tuple.tuple("test title", hashtag2.getHashtagName())
                );
    }

    private List<ArticleHashtag> getArticleHashtags(List<Hashtag> hashtags) {
        Long id = 1L;
        List<ArticleHashtag> articleHashtags = new ArrayList<>();
        for (Hashtag hashtag : hashtags) {
            articleHashtags.add(getArticleHashtag(hashtag, id));
            id++;
        }
        return articleHashtags;
    }

    private ArticleHashtag getArticleHashtag(Hashtag hashtag, Long id) {
        return ArticleHashtag.builder()
                .id(id)
                .hashtag(hashtag)
                .build();
    }

    private Hashtag getHashtag(Long id, String hashtagName) {
        return Hashtag.builder()
                .id(id)
                .hashtagName(hashtagName)
                .build();
    }

    private Article getArticle(UserAccount userAccount) {
        return Article.builder()
                .userAccount(userAccount)
                .title("test title")
                .content("test content")
                .articleHashtags(new LinkedHashSet<>())
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