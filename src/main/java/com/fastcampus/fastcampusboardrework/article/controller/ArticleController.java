package com.fastcampus.fastcampusboardrework.article.controller;

import com.fastcampus.fastcampusboardrework.article.controller.dto.ArticleResponse;
import com.fastcampus.fastcampusboardrework.article.controller.dto.ArticleWithCommentsResponse;
import com.fastcampus.fastcampusboardrework.article.controller.dto.SearchArticleCondition;
import com.fastcampus.fastcampusboardrework.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public String articles(@ModelAttribute SearchArticleCondition condition, @PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<ArticleResponse> articlePage = articleService.getArticlePage(condition.searchType(), condition.searchKeyword(), pageable)
                .map(ArticleResponse::from);

        model.addAttribute("articles", articlePage);

        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, Model model) {
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));

        model.addAttribute("article", article);
        model.addAttribute("articleComments", article.articleComments());

        return "articles/detail";
    }
}
