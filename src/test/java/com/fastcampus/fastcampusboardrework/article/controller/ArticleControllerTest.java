package com.fastcampus.fastcampusboardrework.article.controller;

import com.fastcampus.fastcampusboardrework.article.service.ArticleService;
import com.fastcampus.fastcampusboardrework.article.service.PaginationService;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleCommentDtos;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleWithCommentsDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.UserAccountDto;
import com.fastcampus.fastcampusboardrework.common.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private ArticleService articleService;
    @MockBean private PaginationService paginationService;

    @DisplayName("게시글 리스트 페이지 호출")
    @Test
    public void getArticles() throws Exception {
        Page<ArticleDto> articles = new PageImpl<>(List.of());

        given(articleService.getArticlePage(any(), anyString(), any(Pageable.class)))
                .willReturn(articles);
        given(paginationService.getPaginationBarNumbers(0, 10))
                .willReturn(List.of(0, 1, 2, 3, 4));

        mvc.perform(get("/articles")
                        .param("searchType", "TITLE")
                        .param("searchValue", "Title")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "title" + "+" + "asc")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(view().name("articles/index"))
                .andDo(print());
    }

    @DisplayName("게시글 해시태그 검색 페이지")
    @Test
    public void getHashtags() throws Exception {
        ArticleWithCommentsDto article = ArticleWithCommentsDto.builder()
                .id(1L)
                .userAccountDto(getUserAccountDto())
                .articleComments(getArticleCommentsDto())
                .build();

        given(articleService.getArticle(1L))
                .willReturn(article);
        given(articleService.getArticleCount())
                .willReturn(10L);

        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(view().name("articles/detail"))
                .andDo(print());
    }

    private ArticleCommentDtos getArticleCommentsDto() {
        return ArticleCommentDtos.builder()
                .items(Set.of())
                .build();
    }

    private UserAccountDto getUserAccountDto() {
        return UserAccountDto.builder()
                .id(1L)
                .userId("foo")
                .nickname("bar")
                .build();
    }
}