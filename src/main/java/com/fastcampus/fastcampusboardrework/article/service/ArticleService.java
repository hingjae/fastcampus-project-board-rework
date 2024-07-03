package com.fastcampus.fastcampusboardrework.article.service;

import com.fastcampus.fastcampusboardrework.article.controller.SearchType;
import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.repository.ArticleRepository;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleCreateDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleModifyDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleWithCommentsDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> getArticlePage(SearchType searchType, String searchKeyword, Pageable pageable) {
        return articleRepository.findArticlePageBySearchParam(searchType, searchKeyword, pageable)
                .map(ArticleDto::from);
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        Article article = articleRepository.findByIdWithUserAccountAndArticleComments(articleId)
                .orElseThrow(EntityNotFoundException::new);

        return ArticleWithCommentsDto.from(article);
    }

    @Transactional
    public Long create(ArticleCreateDto dto) {
        return articleRepository.save(dto.toEntity())
                .getId();
    }

    @Transactional
    public void modify(Long articleId, ArticleModifyDto dto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(EntityNotFoundException::new);

        article.modify(dto.title(), dto.content(), dto.hashtag());
    }

    @Transactional
    public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
    }
}
