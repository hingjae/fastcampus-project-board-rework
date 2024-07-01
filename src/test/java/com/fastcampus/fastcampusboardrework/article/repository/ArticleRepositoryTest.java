package com.fastcampus.fastcampusboardrework.article.repository;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import com.fastcampus.fastcampusboardrework.useraccount.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ArticleRepositoryTest {

    @Autowired ArticleRepository articleRepository;
    @Autowired UserAccountRepository userAccountRepository;

    @DisplayName("[JPA 연결 테스트] Article을 생성한다.")
    @Test
    public void save() {
        UserAccount savedUser = userAccountRepository.save(getUserAccount());
        Article article = getArticle(savedUser);

        assertThatCode(() -> articleRepository.save(article))
                .doesNotThrowAnyException();
    }

    private Article getArticle(UserAccount userAccount) {
        return Article.builder()
                .userAccount(userAccount)
                .title("test title")
                .content("test content")
                .hashtag("test tag")
                .build();
    }

    private UserAccount getUserAccount() {
        return UserAccount.builder()
                .userId("honey")
                .userPassword("pw1")
                .email("email1")
                .nickname("nickname1")
                .memo("memo1")
                .build();
    }

    @DisplayName("[JPA 연결 테스트] Article을 조회한다.")
    @Test
    public void find() {
        UserAccount savedUser = userAccountRepository.save(getUserAccount());
        Article article = getArticle(savedUser);

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
        UserAccount savedUser = userAccountRepository.save(getUserAccount());
        Article article = getArticle(savedUser);
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
        UserAccount savedUser = userAccountRepository.save(getUserAccount());
        Article article = getArticle(savedUser);
        Article savedArticle = articleRepository.save(article);

        articleRepository.deleteById(savedArticle.getId());
        Optional<Article> articleOptional = articleRepository.findById(savedArticle.getId());

        assertThat(articleOptional.isPresent()).isFalse();
    }
}