package com.fastcampus.fastcampusboardrework.article.controller;

import com.fastcampus.fastcampusboardrework.article.controller.dto.request.SaveArticleRequest;
import com.fastcampus.fastcampusboardrework.article.service.ArticleService;
import com.fastcampus.fastcampusboardrework.article.service.PaginationService;
import com.fastcampus.fastcampusboardrework.article.service.dto.*;
import com.fastcampus.fastcampusboardrework.common.config.TestSecurityConfig;
import com.fastcampus.fastcampusboardrework.security.CustomUserDetails;
import com.fastcampus.fastcampusboardrework.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Import({FormDataEncoder.class, TestSecurityConfig.class})
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

    @WithMockUser
    @DisplayName("게시글 상세조회 페이지를 반환한다.")
    @Test
    public void getArticleDetail() throws Exception {
        ArticleWithCommentsDto article = ArticleWithCommentsDto.builder()
                .id(1L)
                .userAccountDto(getUserAccountDto())
                .articleComments(getArticleCommentsDto())
                .hashtags(getHashtagsDto())
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

    private HashtagDtos getHashtagsDto() {
        return HashtagDtos.builder()
                .hashtagDtos(Set.of())
                .build();
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
                .andExpect(view().name("articles/search-hashtag"))
                .andDo(print());
    }

    @WithMockUser
    @DisplayName("게시글 생성 페이지를 반환한다.")
    @Test
    public void getCreateArticleFormPage() throws Exception {
        mvc.perform(get("/articles/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("formStatus"))
                .andExpect(view().name("articles/form"))
                .andDo(print());
    }

    @WithUserDetails(value = "userId", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("게시글을 생성한다.")
    @Test
    public void createArticle() throws Exception {
        SaveArticleRequest request = getSaveArticleRequest();
        given(articleService.create("userId", request.toCreateDto()))
                .willReturn(1L);

        mvc.perform(post("/articles/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(formDataEncoder.encode(request))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"))
                .andDo(print());
    }

    @WithMockUser
    @DisplayName("게시글 수정 페이지를 반환한다.")
    @Test
    public void getModifyArticleFormPage() throws Exception {
        given(articleService.getByIdWithUserAccount(1L))
                .willReturn(getModifyArticleDto());

        mvc.perform(get("/articles/{articleId}/form", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("formStatus"))
                .andExpect(view().name("articles/form"))
                .andDo(print());
    }

    private ModifyArticleDto getModifyArticleDto() {
        return ModifyArticleDto.builder()
                .id(1L)
                .title("title")
                .content("content")
                .build();
    }

    @DisplayName("SaveArticleRequest 로 게시글을 수정한다.")
    @Test
    public void modifyArticle() throws Exception {
        SaveArticleRequest request = getSaveArticleRequest();
        Long articleId = 1L;
        willDoNothing().given(articleService).modify(articleId, "userId", request.toModifyDto());

        mvc.perform(post("/articles/{articleId}/form", articleId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(formDataEncoder.encode(request))
                .with(csrf())
                .with(user(CustomUserDetails.builder()
                        .username("userId")
                        .build()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId))
                .andDo(print());
    }

    @WithUserDetails(value = "userId",userDetailsServiceBeanName = "userDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
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
                .build();
    }

    private ArticleDto getArticleDto() {
        return ArticleDto.builder()
                .id(1L)
                .userAccountDto(getUserAccountDto())
                .title("title")
                .content("content")
                .hashtags(getHashtagsDto())
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