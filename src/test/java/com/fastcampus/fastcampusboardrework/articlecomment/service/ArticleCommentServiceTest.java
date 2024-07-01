package com.fastcampus.fastcampusboardrework.articlecomment.service;

import com.fastcampus.fastcampusboardrework.article.repository.ArticleRepository;
import com.fastcampus.fastcampusboardrework.useraccount.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ArticleCommentServiceTest {

    @Autowired private ArticleCommentService articleCommentService;
    @Autowired private ArticleRepository articleRepository;
    @Autowired private UserAccountRepository userAccountRepository;

    @DisplayName("게시글의 댓글을 조회한다.")
    @Test
    public void getArticleWithComments() {
        initData();

//        articleCommentService.searchArticleComments()
    }

    private void initData() {
//        UserAccount.builder()

    }
}