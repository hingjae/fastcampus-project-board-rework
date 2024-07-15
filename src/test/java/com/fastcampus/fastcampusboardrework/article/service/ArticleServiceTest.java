package com.fastcampus.fastcampusboardrework.article.service;

import com.fastcampus.fastcampusboardrework.article.controller.SearchType;
import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.domain.ArticleHashtag;
import com.fastcampus.fastcampusboardrework.article.domain.exception.UserNotAuthorizedException;
import com.fastcampus.fastcampusboardrework.article.repository.ArticleHashtagRepository;
import com.fastcampus.fastcampusboardrework.article.repository.ArticleRepository;
import com.fastcampus.fastcampusboardrework.article.service.dto.*;
import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import com.fastcampus.fastcampusboardrework.articlecomment.repository.ArticleCommentRepository;
import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import com.fastcampus.fastcampusboardrework.useraccount.repository.UserAccountRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import org.assertj.core.groups.Tuple;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("test")
@SpringBootTest
class ArticleServiceTest {

    @Autowired private ArticleService articleService;
    @Autowired private UserAccountRepository userAccountRepository;
    @Autowired private ArticleRepository articleRepository;
    @Autowired private ArticleCommentRepository articleCommentRepository;
    @Autowired private EntityManager em;
    @Autowired
    private ArticleHashtagRepository articleHashtagRepository;

    @Transactional
    @DisplayName("게시글을 생성한다.")
    @Test
    public void createArticle() {
        UserAccount userAccount = saveUser("user1", "aaa@bbb.com");
        CreateArticleDto dto = getCreateArticleDto();

        Long savedArticleId = articleService.create(userAccount.getUserId(), dto);

        Article article = articleRepository.findById(savedArticleId)
                .orElseThrow(EntityExistsException::new);

        assertThat(article).isNotNull();
    }

    @Transactional
    @DisplayName("Hashtag가 포함된 게시글을 생성한다.")
    @Test
    public void createArticleWithHashtag() {
        UserAccount userAccount = saveUser("user1", "aaa@bbb.com");
        CreateArticleDto dto = getCreateArticleDto("title", "foo bar hello world #java #foo #bar");

        Long savedArticleId = articleService.create(userAccount.getUserId(), dto);

        flushAndClear();
        Article article = articleRepository.findById(savedArticleId)
                .orElseThrow(EntityExistsException::new);

        assertThat(article).isNotNull();
        assertThat(article.getTitle()).isEqualTo("title");
        assertThat(article.getContent()).isEqualTo("foo bar hello world #java #foo #bar");
        assertThat(article.getArticleHashtags()).hasSize(3)
                .extracting(articleHashtag -> articleHashtag.getHashtag().getHashtagName())
                .containsExactlyInAnyOrder("foo", "bar", "java");
    }

    @Transactional
    @DisplayName("게시글을 아이디로 상세 조회한다.")
    @Test
    public void getArticleWithComments() {
        UserAccount savedUserAccount = saveUser("user1", "aaa@bbb.com");
        Article savedArticle = saveArticle(savedUserAccount);
        ArticleComment savedArticleComment = saveArticleComment(savedArticle, savedUserAccount);
        flushAndClear();

        ArticleWithCommentsDto article = articleService.getArticleWithComments(savedArticle.getId());

        assertThat(article.id()).isEqualTo(savedArticle.getId());
        assertThat(article.title()).isEqualTo(savedArticle.getTitle());
        assertThat(article.content()).isEqualTo(savedArticle.getContent());
        assertThat(article.articleComments().items()).hasSize(1)
                .extracting(ArticleCommentDto::content)
                .containsExactly(savedArticleComment.getContent());
    }

    @Transactional
    @DisplayName("게시글을 수정 한다.")
    @Test
    public void modifyArticle() {
        UserAccount savedUserAccount = saveUser("user1", "aaa@bbb.com");
        Article savedArticle = saveArticle(savedUserAccount);
        flushAndClear();

        ModifyArticleDto articleModify = getModifyArticleDto();
        articleService.modify(savedArticle.getId(), savedUserAccount.getUserId(), articleModify);
        flushAndClear();

        Article result = articleRepository.findById(savedArticle.getId())
                .orElseThrow(RuntimeException::new);
        assertThat(result.getTitle()).isEqualTo(articleModify.title());
        assertThat(result.getContent()).isEqualTo(articleModify.content());
    }

    @Transactional
    @DisplayName("게시글을 수정한다. 해시테그 내용도 수정된다.")
    @Test
    public void modifyArticleWithHashtags() {
        UserAccount savedUserAccount = saveUser("user1", "aaa@bbb.com");
        Article savedArticle = saveArticle(savedUserAccount);
        flushAndClear();

        ModifyArticleDto dto = getModifyArticleDto("new title", "hello world #java #foo #world");
        articleService.modify(savedArticle.getId(), savedUserAccount.getUserId(), dto);
        flushAndClear();

        Article result = articleRepository.findById(savedArticle.getId())
                .orElseThrow(RuntimeException::new);
        assertThat(result.getTitle()).isEqualTo(dto.title());
        assertThat(result.getContent()).isEqualTo(dto.content());
        assertThat(result.getArticleHashtags()).hasSize(3)
                .extracting(articleHashtag -> articleHashtag.getHashtag().getHashtagName())
                .containsExactlyInAnyOrder("java", "foo", "world");
    }

    @Transactional
    @DisplayName("게시글을 수정한다. 기존의 해시태그는 지운다.")
    @Test
    public void modifyArticleWithHashtags2() {
        UserAccount savedUserAccount = saveUser("user1", "aaa@bbb.com");
        Article savedArticle = articleRepository.save(getArticleByParam(savedUserAccount, "title", "hello world #java #foo #world"));
        flushAndClear();

        ModifyArticleDto dto = getModifyArticleDto("new title", "hello world #java #foo #bar");
        articleService.modify(savedArticle.getId(), savedUserAccount.getUserId(), dto);
        flushAndClear();

        Article result = articleRepository.findById(savedArticle.getId())
                .orElseThrow(RuntimeException::new);
        assertThat(result.getTitle()).isEqualTo(dto.title());
        assertThat(result.getContent()).isEqualTo(dto.content());
        assertThat(result.getArticleHashtags()).hasSize(3)
                .extracting(articleHashtag -> articleHashtag.getHashtag().getHashtagName())
                .containsExactlyInAnyOrder("java", "foo", "bar");
    }

    @Transactional
    @DisplayName("기존의 해시태그는 지우고 새로운 해시테그로 게시글을 수정한다. ")
    @Test
    public void modifyArticleWithHashtags3() {
        UserAccount savedUserAccount = saveUser("user1", "aaa@bbb.com");
        Article savedArticle = articleRepository.save(getArticleByParam(savedUserAccount, "title", "hello world #java #foo #world"));
        flushAndClear();

        ModifyArticleDto dto = getModifyArticleDto("new title", "hello world java foo bar");
        articleService.modify(savedArticle.getId(), savedUserAccount.getUserId(), dto);
        flushAndClear();

        Article result = articleRepository.findById(savedArticle.getId())
                .orElseThrow(RuntimeException::new);
        assertThat(result.getTitle()).isEqualTo(dto.title());
        assertThat(result.getContent()).isEqualTo(dto.content());
        assertThat(result.getArticleHashtags()).isEmpty();
    }

    @Transactional
    @DisplayName("게시글의 유저가 다르면 게시글 수정을 할 수 없다.")
    @Test
    public void modifyArticleFail() {
        UserAccount savedUserAccount1 = saveUser("user1", "aaa@bbb.com");
        UserAccount savedUserAccount2 = saveUser("user2", "bbb@ccc.com");
        Article savedArticle = saveArticle(savedUserAccount1);
        flushAndClear();

        ModifyArticleDto articleModify = getModifyArticleDto();
        assertThatThrownBy(() -> articleService.modify(savedArticle.getId(), savedUserAccount2.getUserId(), articleModify))
                .isInstanceOf(UserNotAuthorizedException.class);
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }

    @Transactional
    @DisplayName("게시글을 삭제 한다.")
    @Test
    public void deleteArticle() {
        UserAccount savedUserAccount = saveUser("user1", "aaa@bbb.com");
        Article savedArticle = articleRepository.save(getArticleByParam(savedUserAccount, "title", "hello world #java #foo #world"));
        flushAndClear();

        articleService.delete(savedArticle.getId(), "user1");
        flushAndClear();

        Optional<Article> articleOptional = articleRepository.findById(savedArticle.getId());
        List<ArticleHashtag> articleHashtagRepositoryAll = articleHashtagRepository.findAll();
        assertThat(articleOptional.isPresent()).isFalse();
        assertThat(articleHashtagRepositoryAll).isEmpty();
    }

    @Transactional
    @DisplayName("게시글을 페이지로 조회한다.")
    @Test
    public void getArticlePage() {
        initArticlePageTestData();

        Page<ArticleDto> articlePage = articleService.getArticlePage(null, null, PageRequest.of(0, 10));

        assertThat(articlePage.getTotalElements()).isEqualTo(20);
        assertThat(articlePage.getContent()).hasSize(10)
                .extracting(ArticleDto::title, ArticleDto::content)
                .containsExactlyInAnyOrder(
                        tuple("foo title0", "foo content0"),
                        tuple("bar title1", "bar content1"),
                        tuple("hello title2", "hello content2"),
                        tuple("foo title3", "foo content3"),
                        tuple("bar title4", "bar content4"),
                        tuple("hello title5", "hello content5"),
                        tuple("foo title6", "foo content6"),
                        tuple("bar title7", "bar content7"),
                        tuple("hello title8", "hello content8"),
                        tuple("foo title9", "foo content9")
                );
    }

    @Transactional
    @DisplayName("검색 조건으로 게시글을 페이지로 조회한다.")
    @Test
    public void getArticlePageByParam() {
        initArticlePageTestData();
        Page<ArticleDto> articlePage = articleService.getArticlePage(SearchType.TITLE, "foo", PageRequest.of(0, 10));

        assertThat(articlePage.getTotalElements()).isEqualTo(7);
        assertThat(articlePage.getContent()).hasSize(7)
                .extracting(ArticleDto::title, ArticleDto::content)
                .containsExactlyInAnyOrder(
                        Tuple.tuple("foo title0", "foo content0"),
                        Tuple.tuple("foo title3", "foo content3"),
                        Tuple.tuple("foo title6", "foo content6"),
                        Tuple.tuple("foo title9", "foo content9"),
                        Tuple.tuple("foo title12", "foo content12"),
                        Tuple.tuple("foo title15", "foo content15"),
                        Tuple.tuple("foo title18", "foo content18")
                );
    }

    @Transactional
    @DisplayName("정렬 조건으로 게시글을 페이지로 조회한다.")
    @Test
    public void getArticlePageByOrder() {
        initArticlePageTestData();
        Page<ArticleDto> articlePage = articleService.getArticlePage(null, null, PageRequest.of(0, 10, Sort.by("content").descending()));

        assertThat(articlePage.getTotalElements()).isEqualTo(20);
        assertThat(articlePage.getContent()).hasSize(10)
                .extracting(ArticleDto::title, ArticleDto::content)
                .containsExactly(
                        Tuple.tuple("hello title8", "hello content8"),
                        Tuple.tuple("hello title5", "hello content5"),
                        Tuple.tuple("hello title2", "hello content2"),
                        Tuple.tuple("hello title17", "hello content17"),
                        Tuple.tuple("hello title14", "hello content14"),
                        Tuple.tuple("hello title11", "hello content11"),
                        Tuple.tuple("foo title9", "foo content9"),
                        Tuple.tuple("foo title6", "foo content6"),
                        Tuple.tuple("foo title3", "foo content3"),
                        Tuple.tuple("foo title18", "foo content18")
                );
    }

    @Transactional
    @DisplayName("articleId로 게시글을 조회한다.")
    @Test
    public void getByArticleIdTest() {
        Article article = saveArticle(saveUser("fooUserId", "fooEmail"));
        flushAndClear();

        ModifyArticleDto result = articleService.getByIdWithUserAccount(article.getId());

        assertThat(result.title()).isEqualTo(article.getTitle());
        assertThat(result.content()).isEqualTo(article.getContent());
    }

    private void initArticlePageTestData() {
        UserAccount user1 = saveUser("user1", "aaa@bbb.com");
        UserAccount user2 = saveUser("user2", "bbb@ccc.com");
        for (int i = 0; i < 20; i++) {
            UserAccount targetUserAccount;
            if (i % 2 == 0) {
                targetUserAccount = user1;
            } else {
                targetUserAccount = user2;
            }

            String articleTitle;
            String articleContent;
            if (i % 3 == 0) {
                articleTitle = "foo title" + i;
                articleContent = "foo content" + i;
            } else if (i % 3 == 1) {
                articleTitle = "bar title" + i;
                articleContent = "bar content" + i;
            } else {
                articleTitle = "hello title" + i;
                articleContent = "hello content" + i;
            }

            saveArticleByParam(targetUserAccount, articleTitle, articleContent);
        }

        flushAndClear();
    }

    private void saveArticleByParam(UserAccount user, String articleTitle, String articleContent) {
        Article article = getArticleByParam(user, articleTitle, articleContent);
        articleRepository.save(article);
    }

    private Article getArticleByParam(UserAccount user, String articleTitle, String articleContent) {
        return Article.builder()
                .userAccount(user)
                .title(articleTitle)
                .content(articleContent)
                .build();
    }

    private ModifyArticleDto getModifyArticleDto() {
        return ModifyArticleDto.builder()
                .title("new title")
                .content("new content")
                .build();
    }

    private ModifyArticleDto getModifyArticleDto(String title, String content) {
        return ModifyArticleDto.builder()
                .title(title)
                .content(content)
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
                .build();
    }

    private Set<ArticleHashtag> getArticleHashtags() {
        return Set.of(
                ArticleHashtag.builder()
                        .id(1L)
                        .hashtag(getHashtag())
                        .build()
        );
    }

    private Hashtag getHashtag() {
        return Hashtag.builder()
                .id(1L)
                .hashtagName("hashtag")
                .build();
    }

    private Article getArticle() {
        return Article.builder()
                .id(1L)
                .title("title")
                .userAccount(getUser("user1", "email"))
                .build();
    }

    private ArticleComment getArticleComment(Article savedArticle, UserAccount userAccount) {
        return ArticleComment.builder()
                .article(savedArticle)
                .userAccount(userAccount)
                .content("comment comment")
                .build();
    }

    private CreateArticleDto getCreateArticleDto() {
        return CreateArticleDto.builder()
                .title("article title")
                .content("article content")
                .build();
    }

    private CreateArticleDto getCreateArticleDto(String title, String content) {
        return CreateArticleDto.builder()
                .title(title)
                .content(content)
                .build();
    }

    private UserAccount saveUser(String userId, String email) {
        return userAccountRepository.save(getUser(userId, email));
    }

    private UserAccount getUser(String userId, String email) {
        return UserAccount.builder()
                .userId(userId)
                .userPassword("pw1")
                .email(email)
                .nickname("honey")
                .memo("memo")
                .build();
    }
}
