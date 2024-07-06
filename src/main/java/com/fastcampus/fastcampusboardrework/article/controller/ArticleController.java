package com.fastcampus.fastcampusboardrework.article.controller;

import com.fastcampus.fastcampusboardrework.article.controller.dto.ArticleResponse;
import com.fastcampus.fastcampusboardrework.article.controller.dto.ArticleWithCommentsResponse;
import com.fastcampus.fastcampusboardrework.article.controller.dto.SearchArticleCondition;
import com.fastcampus.fastcampusboardrework.article.service.ArticleService;
import com.fastcampus.fastcampusboardrework.article.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final PaginationService paginationService;

    @GetMapping
    public String articles(@RequestParam(required = false) SearchType searchType,
                           @RequestParam(required = false) String searchValue,
                           @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<ArticleResponse> articlePage = articleService.getArticlePage(searchType, searchValue, pageable)
                .map(ArticleResponse::from);

        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articlePage.getTotalPages());

        model.addAttribute("articles", articlePage);
        model.addAttribute("paginationBarNumbers", barNumbers);
        model.addAttribute("searchTypes", SearchType.values());

        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, Model model) {
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));

        model.addAttribute("article", article);
        model.addAttribute("articleComments", article.articleComments());
        model.addAttribute("totalCount", articleService.getArticleCount()); // 마지막 게시글일 경우 다음 게시글 버튼 비활성화

        return "articles/detail";
    }
}
