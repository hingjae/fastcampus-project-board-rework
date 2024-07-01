package com.fastcampus.fastcampusboardrework.article.service;

import com.fastcampus.fastcampusboardrework.article.controller.SearchType;
import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.repository.ArticleRepository;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleCreateDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleModifyDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleWithCommentsDto;
import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return articleRepository.findByIdWithUserAccountAndArticleComments(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(EntityNotFoundException::new);
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
