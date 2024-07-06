package com.fastcampus.fastcampusboardrework.article.repository;

import com.fastcampus.fastcampusboardrework.article.controller.SearchType;
import com.fastcampus.fastcampusboardrework.article.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleRepositoryCustom {
    Page<Article> findArticlePageBySearchParam(SearchType searchType, String searchKeyword, Pageable pageable);

    Page<Article> findByHashtag(String searchValue, Pageable pageable);

    List<String> findAllHashtagLimit100();
}
