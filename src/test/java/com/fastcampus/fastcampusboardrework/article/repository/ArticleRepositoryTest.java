package com.fastcampus.fastcampusboardrework.article.repository;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ArticleRepositoryTest {

    @Autowired ArticleRepository articleRepository;

    @DisplayName("[JPA 연결 테스트] Article을 생성한다.")
    @Test
    public void save() {
        Article article = Article.builder()
                .title("test title")
                .content("test content")
                .hashtag("test tag")
                .build();

        assertThatCode(() -> articleRepository.save(article))
                .doesNotThrowAnyException();
    }

    @DisplayName("[JPA 연결 테스트] Article을 조회한다.")
    @Test
    public void find() {
        Article article = Article.builder()
                .title("test title")
                .content("test content")
                .hashtag("test tag")
                .build();

        Article savedArticle = articleRepository.save(article);

        Optional<Article> articleOptional = articleRepository.findById(savedArticle.getId());

        assertThat(articleOptional.isPresent()).isTrue();
        assertThat(articleOptional.get()).isEqualTo(savedArticle);
    }

    @DisplayName("[JPA 연결 테스트] Article을 업데이트 한다.")
    @Test
    public void update() {
        String newTitle = "new title";
        String newContent = "new content";
        Article article = Article.builder()
                .title("test title")
                .content("test content")
                .hashtag("test tag")
                .build();
        Article savedArticle = articleRepository.save(article);

        Article findArticle = articleRepository.findById(savedArticle.getId()).get();
        findArticle.setTitle(newTitle);
        findArticle.setContent(newContent);
        articleRepository.flush();
        Article updatedArticle = articleRepository.findById(findArticle.getId()).get();

        assertThat(updatedArticle.getTitle()).isEqualTo(newTitle);
        assertThat(updatedArticle.getContent()).isEqualTo(newContent);
    }

    @DisplayName("[JPA 연결 테스트] Article을 삭제한다.")
    @Test
    public void delete() {
        Article article = Article.builder()
                .title("test title")
                .content("test content")
                .hashtag("test tag")
                .build();
        Article savedArticle = articleRepository.save(article);

        articleRepository.deleteById(savedArticle.getId());
        Optional<Article> articleOptional = articleRepository.findById(savedArticle.getId());

        assertThat(articleOptional.isPresent()).isFalse();
    }
}