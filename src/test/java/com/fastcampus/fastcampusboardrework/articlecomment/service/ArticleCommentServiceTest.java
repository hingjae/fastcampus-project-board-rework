package com.fastcampus.fastcampusboardrework.articlecomment.service;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.repository.ArticleRepository;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleCommentDto;
import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import com.fastcampus.fastcampusboardrework.articlecomment.repository.ArticleCommentRepository;
import com.fastcampus.fastcampusboardrework.articlecomment.service.dto.CreateArticleCommentDto;
import com.fastcampus.fastcampusboardrework.articlecomment.service.dto.ModifyArticleCommentDto;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import com.fastcampus.fastcampusboardrework.useraccount.repository.UserAccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ArticleCommentServiceTest {

    @Autowired private ArticleCommentService articleCommentService;
    @Autowired private ArticleRepository articleRepository;
    @Autowired private UserAccountRepository userAccountRepository;
    @Autowired private ArticleCommentRepository articleCommentRepository;
    @Autowired private EntityManager em;

    @Transactional
    @DisplayName("댓글을 생성한다.")
    @Test
    public void createArticleCommentTest() {
        UserAccount userAccount = userAccountRepository.save(getUserAccount());
        Article article = articleRepository.save(getArticle(userAccount));
        flushAndClear();
        CreateArticleCommentDto createArticleCommentDto = getCreateArticleCommentDto();

        articleCommentService.create(article.getId(), userAccount.getUserId(), createArticleCommentDto);

        Set<ArticleComment> articleComments = articleCommentRepository.findByArticle_Id(article.getId());
        assertThat(articleComments).hasSize(1)
                .extracting(ArticleComment::getContent)
                .containsExactly("foo content");
    }

    private static CreateArticleCommentDto getCreateArticleCommentDto() {
        return CreateArticleCommentDto.builder()
                .content("foo content")
                .build();
    }

    @Transactional
    @DisplayName("게시글의 댓글을 조회한다.")
    @Test
    public void getArticleWithComments() {
        Long articleId = initData();

        Set<ArticleCommentDto> items = articleCommentService.getByArticleId(articleId).items();

        assertThat(items).hasSize(2)
                .extracting(ArticleCommentDto::content, item -> item.userAccountDto().userId(), item -> item.userAccountDto().nickname())
                .containsExactlyInAnyOrder(
                        Tuple.tuple("foo content", "fooUserId", "fooNickname"),
                        Tuple.tuple("bar content", "fooUserId", "fooNickname")
                );
    }

    @Transactional
    @DisplayName("댓글을 수정한다.")
    @Test
    public void modifyArticleCommentTest() {
        UserAccount userAccount = userAccountRepository.save(getUserAccount());
        Article article = articleRepository.save(getArticle(userAccount));
        ArticleComment savedArticleComment = articleCommentRepository.save(getArticleComment(userAccount, article, "before modify"));
        String newContent = "after modify";
        ModifyArticleCommentDto modifyArticleCommentDto = getModifyArticleCommentDto(newContent);

        articleCommentService.modify(savedArticleComment.getId(), userAccount.getUserId(), modifyArticleCommentDto);
        flushAndClear();

        ArticleComment result = articleCommentRepository.findById(savedArticleComment.getId())
                .orElseThrow(EntityNotFoundException::new);
        Assertions.assertThat(result.getContent()).isEqualTo(newContent);

    }

    @Transactional
    @DisplayName("댓글을 삭제한다.")
    @Test
    public void deleteArticleCommentTest() {
        UserAccount userAccount = userAccountRepository.save(getUserAccount());
        Article article = articleRepository.save(getArticle(userAccount));
        ArticleComment savedArticleComment = articleCommentRepository.save(getArticleComment(userAccount, article, "foo"));

        articleCommentService.delete(savedArticleComment.getId());
        flushAndClear();

        Optional<ArticleComment> articleCommentOptional = articleCommentRepository.findById(savedArticleComment.getId());
        assertThat(articleCommentOptional).isEmpty();
    }


    private ModifyArticleCommentDto getModifyArticleCommentDto(String content) {
        return ModifyArticleCommentDto.builder()
                .content(content)
                .build();
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }

    private Long initData() {
        UserAccount userAccount = userAccountRepository.save(getUserAccount());
        Article article = articleRepository.save(getArticle(userAccount));
        ArticleComment articleComment1 = getArticleComment(userAccount, article, "foo content");
        ArticleComment articleComment2 = getArticleComment(userAccount, article, "bar content");
        articleCommentRepository.save(articleComment1);
        articleCommentRepository.save(articleComment2);

        flushAndClear();

        return article.getId();
    }

    private ArticleComment getArticleComment(UserAccount userAccount, Article article, String content) {
        return ArticleComment.builder()
                .userAccount(userAccount)
                .article(article)
                .content(content)
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