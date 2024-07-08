package com.fastcampus.fastcampusboardrework.articlecomment.controller;

import com.fastcampus.fastcampusboardrework.articlecomment.controller.dto.request.ArticleCommentRequest;
import com.fastcampus.fastcampusboardrework.articlecomment.service.ArticleCommentService;
import com.fastcampus.fastcampusboardrework.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        articleCommentService.create(customUserDetails.username(), articleCommentRequest.toDto());

        return "redirect:/articles/" + articleCommentRequest.articleId();
    }

    @PostMapping ("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId, Long articleId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        articleCommentService.delete(commentId, customUserDetails.username());

        return "redirect:/articles/" + articleId;
    }

}
