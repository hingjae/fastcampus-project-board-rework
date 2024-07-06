package com.fastcampus.fastcampusboardrework.articlecomment.service.dto;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

        ArticleComment articleComment = createArticleCommentDto.toEntity(article, userAccount);

        assertThat(articleComment.getContent()).isEqualTo("foo content");
        assertThat(articleComment.getArticle()).isEqualTo(article);
        assertThat(articleComment.getUserAccount()).isEqualTo(userAccount);
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