package com.fastcampus.fastcampusboardrework.article.repository;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.domain.ArticleHashtag;
import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import com.fastcampus.fastcampusboardrework.hashtag.repository.HashtagRepository;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import com.fastcampus.fastcampusboardrework.useraccount.repository.UserAccountRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ArticleRepositoryTest {

    @Autowired ArticleRepository articleRepository;
    @Autowired UserAccountRepository userAccountRepository;
    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private ArticleHashtagRepository articleHashtagRepository;
    @Autowired
    private EntityManager em;

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

    @DisplayName("hashtag 키워드로 Article을 조회한다.")
    @Test
    public void findByHashTagTest() {
        UserAccount savedUser = userAccountRepository.save(getUserAccount());
        List<Hashtag> hashtags = hashtagRepository.saveAll(getHashtags());
        List<Article> articles = articleRepository.saveAll(getArticles(savedUser));
        initArticleHashtag(articles, hashtags);
        flushAndClear();

        Page<Article> hashtagPage = articleRepository.findByHashtag("hashtag0", PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

        assertThat(hashtagPage).isNotNull();
        assertThat(hashtagPage.getContent()).hasSize(4)
                .extracting(Article::getTitle, Article::getContent)
                .containsExactly(
                        tuple("title0", "content0"),
                        tuple("title1", "content1"),
                        tuple("title2", "content2"),
                        tuple("title9", "content9")
                );
    }

    @DisplayName("Article과 연관된 hashtag가 많은 순으로 조회한다.")
    @Test
    public void findAllHashtagLimit100Test() {
        UserAccount savedUser = userAccountRepository.save(getUserAccount());
        List<Hashtag> hashtags = hashtagRepository.saveAll(getHashtags());
        List<Article> articles = articleRepository.saveAll(getArticles(savedUser));
        initArticleHashtag(articles, hashtags);
        flushAndClear();

        List<String> hashtagNames = articleRepository.findAllHashtagLimit100();

        assertThat(hashtagNames).hasSize(5)
                .containsExactly("hashtag4", "hashtag2", "hashtag0", "hashtag1", "hashtag3");
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }

    private void initArticleHashtag(List<Article> articles, List<Hashtag> hashtags) {
        ArticleHashtag articleHashtag1 = getArticleHashtag(articles.get(0), hashtags.get(0));
        ArticleHashtag articleHashtag2 = getArticleHashtag(articles.get(0), hashtags.get(1));
        ArticleHashtag articleHashtag3 = getArticleHashtag(articles.get(1), hashtags.get(0));
        ArticleHashtag articleHashtag4 = getArticleHashtag(articles.get(1), hashtags.get(1));
        ArticleHashtag articleHashtag5 = getArticleHashtag(articles.get(1), hashtags.get(4));
        ArticleHashtag articleHashtag6 = getArticleHashtag(articles.get(1), hashtags.get(4));
        ArticleHashtag articleHashtag7 = getArticleHashtag(articles.get(2), hashtags.get(0));
        ArticleHashtag articleHashtag8 = getArticleHashtag(articles.get(3), hashtags.get(2));
        ArticleHashtag articleHashtag9 = getArticleHashtag(articles.get(4), hashtags.get(2));
        ArticleHashtag articleHashtag10 = getArticleHashtag(articles.get(4), hashtags.get(3));
        ArticleHashtag articleHashtag11 = getArticleHashtag(articles.get(4), hashtags.get(4));
        ArticleHashtag articleHashtag12 = getArticleHashtag(articles.get(5), hashtags.get(4));
        ArticleHashtag articleHashtag13 = getArticleHashtag(articles.get(6), hashtags.get(4));
        ArticleHashtag articleHashtag14 = getArticleHashtag(articles.get(7), hashtags.get(4));
        ArticleHashtag articleHashtag15 = getArticleHashtag(articles.get(8), hashtags.get(2));
        ArticleHashtag articleHashtag16 = getArticleHashtag(articles.get(8), hashtags.get(4));
        ArticleHashtag articleHashtag17 = getArticleHashtag(articles.get(9), hashtags.get(0));
        ArticleHashtag articleHashtag18 = getArticleHashtag(articles.get(9), hashtags.get(1));
        ArticleHashtag articleHashtag19 = getArticleHashtag(articles.get(9), hashtags.get(2));
        ArticleHashtag articleHashtag20 = getArticleHashtag(articles.get(9), hashtags.get(3));
        ArticleHashtag articleHashtag21 = getArticleHashtag(articles.get(7), hashtags.get(2));
        articleHashtagRepository.save(articleHashtag1);
        articleHashtagRepository.save(articleHashtag2);
        articleHashtagRepository.save(articleHashtag3);
        articleHashtagRepository.save(articleHashtag4);
        articleHashtagRepository.save(articleHashtag5);
        articleHashtagRepository.save(articleHashtag6);
        articleHashtagRepository.save(articleHashtag7);
        articleHashtagRepository.save(articleHashtag8);
        articleHashtagRepository.save(articleHashtag9);
        articleHashtagRepository.save(articleHashtag10);
        articleHashtagRepository.save(articleHashtag11);
        articleHashtagRepository.save(articleHashtag12);
        articleHashtagRepository.save(articleHashtag13);
        articleHashtagRepository.save(articleHashtag14);
        articleHashtagRepository.save(articleHashtag15);
        articleHashtagRepository.save(articleHashtag16);
        articleHashtagRepository.save(articleHashtag17);
        articleHashtagRepository.save(articleHashtag18);
        articleHashtagRepository.save(articleHashtag19);
        articleHashtagRepository.save(articleHashtag20);
        articleHashtagRepository.save(articleHashtag21);
    }

    private ArticleHashtag getArticleHashtag(Article article, Hashtag hashtag) {
        return ArticleHashtag.builder()
                .article(article)
                .hashtag(hashtag)
                .build();
    }

    private List<Hashtag> getHashtags() {
        return List.of(getHashtag("hashtag0"), getHashtag("hashtag1"), getHashtag("hashtag2"), getHashtag("hashtag3"), getHashtag("hashtag4"));
    }

    private List<Article> getArticles(UserAccount savedUser) {
        return List.of(
                getArticle(savedUser, "title0", "content0"),
                getArticle(savedUser, "title1", "content1"),
                getArticle(savedUser, "title2", "content2"),
                getArticle(savedUser, "title3", "content3"),
                getArticle(savedUser, "title4", "content4"),
                getArticle(savedUser, "title5", "content5"),
                getArticle(savedUser, "title6", "content6"),
                getArticle(savedUser, "title7", "content7"),
                getArticle(savedUser, "title8", "content8"),
                getArticle(savedUser, "title9", "content9")
        );
    }

    private Article getArticle(UserAccount savedUser, String title, String content) {
        return Article.builder()
                .userAccount(savedUser)
                .title(title)
                .content(content)
                .build();
    }

    private Hashtag getHashtag(String hashtagName) {
        return Hashtag.builder()
                .hashtagName(hashtagName)
                .build();
    }
}