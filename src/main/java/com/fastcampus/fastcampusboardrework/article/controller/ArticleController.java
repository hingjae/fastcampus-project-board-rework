package com.fastcampus.fastcampusboardrework.article.controller;

import com.fastcampus.fastcampusboardrework.article.controller.dto.request.SaveArticleRequest;
import com.fastcampus.fastcampusboardrework.article.controller.dto.response.ArticleResponse;
import com.fastcampus.fastcampusboardrework.article.controller.dto.response.ArticleWithCommentsResponse;
import com.fastcampus.fastcampusboardrework.article.service.ArticleService;
import com.fastcampus.fastcampusboardrework.article.service.PaginationService;
import com.fastcampus.fastcampusboardrework.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticleWithComments(articleId));

        model.addAttribute("article", article);
        model.addAttribute("articleComments", article.articleComments());
        model.addAttribute("totalCount", articleService.getArticleCount()); // 마지막 게시글일 경우 다음 게시글 버튼 비활성화

        return "articles/detail";
    }

    @GetMapping("/search-hashtag")
    public String searchArticleHashtag(
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<ArticleResponse> articles = articleService.searchArticlesViaHashtag(searchValue, pageable)
                .map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        List<String> hashtags = articleService.getHashtags();

        model.addAttribute("articles", articles);
        model.addAttribute("hashtags", hashtags);
        model.addAttribute("paginationBarNumbers", barNumbers);
        model.addAttribute("searchType", SearchType.HASHTAG);

        return "articles/search-hashtag";
    }

    @GetMapping("/form")
    public String createArticleForm(Model model) {
        model.addAttribute("formStatus", FormStatus.CREATE);

        return "articles/form";
    }

    @PostMapping ("/form")
    public String create(SaveArticleRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        articleService.create(userDetails.getUsername(), request.toCreateDto());

        return "redirect:/articles";
    }

    @GetMapping("/{articleId}/form")
    public String modifyArticleForm(@PathVariable Long articleId, Model model) {
        ArticleResponse article = ArticleResponse.from(articleService.getByIdWithUserAccount(articleId));

        model.addAttribute("article", article);
        model.addAttribute("formStatus", FormStatus.UPDATE);

        return "articles/form";
    }

    @PostMapping ("/{articleId}/form")
    public String modify(@PathVariable Long articleId, SaveArticleRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        articleService.modify(articleId, customUserDetails.getUsername(), request.toModifyDto());

        return "redirect:/articles/" + articleId;
    }

    @PostMapping("/{articleId}/delete")
    public String delete(@PathVariable Long articleId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        articleService.delete(articleId, customUserDetails.getUsername());

        return "redirect:/articles";
    }
}
