package com.fastcampus.fastcampusboardrework.articlecomment.controller;

import com.fastcampus.fastcampusboardrework.articlecomment.controller.dto.request.ArticleCommentRequest;
import com.fastcampus.fastcampusboardrework.articlecomment.service.ArticleCommentService;
import com.fastcampus.fastcampusboardrework.common.config.TestSecurityConfig;
import com.fastcampus.fastcampusboardrework.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({FormDataEncoder.class, TestSecurityConfig.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class ArticleCommentControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private FormDataEncoder formDataEncoder;

    @MockBean private ArticleCommentService articleCommentService;

    @WithUserDetails(value = "userId", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("댓글을 생성한다.")
    @Test
    public void createArticleComment() throws Exception {
        Long articleId = 1L;
        ArticleCommentRequest request = getArticleCommentRequest(articleId);

        given(articleCommentService.create("userId", request.toDto()))
                .willReturn(1L);

        mvc.perform(post("/comments/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(request))
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId))
                .andDo(print());
    }

    @WithUserDetails(value = "userId", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("댓글을 삭제한다.")
    @Test
    public void deleteArticleComment() throws Exception {
        Long articleId = 1L;
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentService).delete(articleCommentId, "userId");

        mvc.perform(post("/comments/{commentId}/delete", articleCommentId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("articleId", String.valueOf(articleId))
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId))
                .andDo(print());
    }

    private ArticleCommentRequest getArticleCommentRequest(Long articleId) {
        return ArticleCommentRequest.builder()
                .articleId(articleId)
                .content("content")
                .build();
    }
}