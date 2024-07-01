package com.fastcampus.fastcampusboardrework.article.service;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.repository.ArticleRepository;
import com.fastcampus.fastcampusboardrework.article.service.dto.*;
import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import com.fastcampus.fastcampusboardrework.articlecomment.repository.ArticleCommentRepository;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import com.fastcampus.fastcampusboardrework.useraccount.repository.UserAccountRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ArticleServiceTest {

    @Autowired private ArticleService articleService;
    @Autowired private UserAccountRepository userAccountRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleCommentRepository articleCommentRepository;
    @Autowired
    private EntityManager em;

    @Transactional
    @DisplayName("게시글을 생성한다.")
    @Test
    public void createArticle() {
        UserAccount userAccount = saveUser();
        ArticleCreateDto dto = getArticleCreateDto(userAccount);

        articleService.create(dto);
    }

    @Transactional
    @DisplayName("게시글을 아이디로 상세 조회한다.")
    @Test
    public void getArticle() {
        UserAccount savedUserAccount = saveUser();
        Article savedArticle = saveArticle(savedUserAccount);
        ArticleComment savedArticleComment = saveArticleComment(savedArticle, savedUserAccount);
        em.flush();
        em.clear(); // 연관관계 편의 메서드를 만들지 않았기 때문에 clear해줘야함.

        ArticleWithCommentsDto article = articleService.getArticle(savedArticle.getId());

        assertThat(article.id()).isEqualTo(savedArticle.getId());
        assertThat(article.title()).isEqualTo(savedArticle.getTitle());
        assertThat(article.content()).isEqualTo(savedArticle.getContent());
        assertThat(article.hashtag()).isEqualTo(savedArticle.getHashtag());
        assertThat(article.articleComments().items()).hasSize(1)
                .extracting(ArticleCommentDto::content)
                .containsExactly(savedArticleComment.getContent());
    }

    @Transactional
    @DisplayName("게시글을 업데이트 한다.")
    @Test
    public void modifyArticle() {
        UserAccount savedUserAccount = saveUser();
        Article savedArticle = saveArticle(savedUserAccount);
        em.flush();
        em.clear();

        ArticleModifyDto articleModify = getArticleModifyDto();
        articleService.modify(savedArticle.getId(), articleModify);

        Article result = articleRepository.findById(savedArticle.getId())
                .orElseThrow(RuntimeException::new);
        assertThat(result.getTitle()).isEqualTo(articleModify.title());
        assertThat(result.getContent()).isEqualTo(articleModify.content());
        assertThat(result.getHashtag()).isEqualTo(articleModify.hashtag());
    }

    @Transactional
    @DisplayName("게시글을 삭제 한다.")
    @Test
    public void deleteArticle() {
        UserAccount savedUserAccount = saveUser();
        Article savedArticle = saveArticle(savedUserAccount);
        em.flush();
        em.clear();

        articleService.delete(savedArticle.getId());

        Optional<Article> articleOptional = articleRepository.findById(savedArticle.getId());
        assertThat(articleOptional.isPresent()).isFalse();
    }

    private ArticleModifyDto getArticleModifyDto() {
        return ArticleModifyDto.builder()
                .title("new title")
                .content("new content")
                .hashtag("new hashtag")
                .build();
    }

    private ArticleComment saveArticleComment(Article savedArticle, UserAccount userAccount) {
        return articleCommentRepository.save(getArticleComment(savedArticle, userAccount));
    }

    private Article saveArticle(UserAccount userAccount) {
        return articleRepository.save(getArticle(userAccount));
    }

    private Article getArticle(UserAccount userAccount) {
        return Article.builder()
                .userAccount(userAccount)
                .title("article title")
                .content("article content")
                .hashtag("hashtag")
                .build();
    }

    private ArticleComment getArticleComment(Article savedArticle, UserAccount userAccount) {
        return ArticleComment.builder()
                .article(savedArticle)
                .userAccount(userAccount)
                .content("comment comment")
                .build();
    }

    private ArticleCreateDto getArticleCreateDto(UserAccount userAccount) {
        return ArticleCreateDto.builder()
                .userAccountDto(UserAccountDto.from(userAccount))
                .title("article title")
                .content("article content")
                .hashtag("hashtag")
                .build();
    }

    private UserAccount saveUser() {
        return userAccountRepository.save(getUser());
    }

    private UserAccount getUser() {
        return UserAccount.builder()
                .userId("honey")
                .userPassword("pw1")
                .email("honey@gmail.com")
                .nickname("honey")
                .memo("memo")
                .build();
    }
}
