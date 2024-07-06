package com.fastcampus.fastcampusboardrework.article.controller;

import com.fastcampus.fastcampusboardrework.article.controller.dto.request.SaveArticleRequest;
import com.fastcampus.fastcampusboardrework.article.service.ArticleService;
import com.fastcampus.fastcampusboardrework.article.service.PaginationService;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleCommentDtos;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleWithCommentsDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.UserAccountDto;
import com.fastcampus.fastcampusboardrework.common.config.SecurityConfig;
import com.fastcampus.fastcampusboardrework.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private FormDataEncoder formDataEncoder;
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
                        .param("sort", "title" + "," + "asc")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(view().name("articles/index"))
                .andDo(print());
    }

    @DisplayName("게시글 상세조회 페이지를 반환한다.")
    @Test
    public void getArticleDetail() throws Exception {
        ArticleWithCommentsDto article = ArticleWithCommentsDto.builder()
                .id(1L)
                .userAccountDto(getUserAccountDto())
                .articleComments(getArticleCommentsDto())
                .build();

        given(articleService.getArticleWithComments(1L))
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

    @DisplayName("헤시테그 검색 페이지를 반환한다.")
    @Test
    public void getHashtagSearchPage() throws Exception {
        Page<ArticleDto> articles = new PageImpl<>(List.of(getArticleDto()));

        given(articleService.searchArticlesViaHashtag(null, PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")))))
                .willReturn(articles);
        given(paginationService.getPaginationBarNumbers(0, 10))
                .willReturn(List.of(0, 1, 2, 3, 4));
        given(articleService.getHashtags())
                .willReturn(List.of());

        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("hashtags"))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attributeExists("searchType"))
                .andExpect(view().name("articles/search-hashtag"))
                .andDo(print());
    }

    @DisplayName("검색 조건으로 헤시테그 검색 페이지를 반환한다.")
    @Test
    public void getHashtagSearchPageByParam() throws Exception {
        Page<ArticleDto> articles = new PageImpl<>(List.of(getArticleDto()));

        given(articleService.searchArticlesViaHashtag("foo", PageRequest.of(0, 10, Sort.by(Sort.Order.asc("createdAt")))))
                .willReturn(articles);
        given(paginationService.getPaginationBarNumbers(0, 10))
                .willReturn(List.of(0, 1, 2, 3, 4));
        given(articleService.getHashtags())
                .willReturn(List.of());

        mvc.perform(get("/articles/search-hashtag")
                .param("searchValue", "foo")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "createdAt" + "," + "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("hashtags"))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attributeExists("searchType"))
                .andExpect(view().name("articles/search-hashtag"))
                .andDo(print());
    }

    @DisplayName("게시글 생성 페이지를 반환한다.")
    @Test
    public void getCreateArticleFormPage() throws Exception {
        mvc.perform(get("/articles/create"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("formStatus"))
                .andExpect(view().name("articles/form"))
                .andDo(print());
    }

    @DisplayName("게시글을 생성한다.")
    @Test
    public void createArticle() throws Exception {
        SaveArticleRequest request = getSaveArticleRequest();
        given(articleService.create("userId", request.toCreateDto()))
                .willReturn(1L);

        mvc.perform(post("/articles/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(formDataEncoder.encode(request))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"))
                .andDo(print());
    }

    @DisplayName("게시글 수정 페이지를 반환한다.")
    @Test
    public void getModifyArticleFormPage() throws Exception {
        given(articleService.getByIdWithUserAccount(1L))
                .willReturn(getArticleDto());

        mvc.perform(get("/articles/{articleId}/modify", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("formStatus"))
                .andExpect(view().name("articles/form"))
                .andDo(print());
    }

    @DisplayName("SaveArticleRequest 로 게시글을 수정한다.")
    @Test
    public void modifyArticle() throws Exception {
        SaveArticleRequest request = getSaveArticleRequest();
        Long articleId = 1L;
        willDoNothing().given(articleService).modify(articleId, "userId", request.toModifyDto());

        mvc.perform(post("/articles/{articleId}/modify", articleId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(formDataEncoder.encode(request))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId))
                .andDo(print());
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    public void deleteArticle() throws Exception {
        Long articleId = 1L;
        String userId = "userId";
        willDoNothing().given(articleService).delete(articleId, userId);

        mvc.perform(post("/articles/{articleId}/delete", articleId)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"))
                .andDo(print());
    }

    private SaveArticleRequest getSaveArticleRequest() {
        return SaveArticleRequest.builder()
                .title("new Title")
                .content("new Content")
                .hashtag("foo")
                .build();
    }

    private ArticleDto getArticleDto() {
        return ArticleDto.builder()
                .id(1L)
                .userAccountDto(getUserAccountDto())
                .title("title")
                .content("content")
                .hashtag("hashtag")
                .build();
    }

    private ArticleCommentDtos getArticleCommentsDto() {
        return ArticleCommentDtos.builder()
                .items(Set.of())
                .build();
    }

    private UserAccountDto getUserAccountDto() {
        return UserAccountDto.builder()
                .userId("foo")
                .nickname("bar")
                .build();
    }
}