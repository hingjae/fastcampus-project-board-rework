package com.fastcampus.fastcampusboardrework.article.repository;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
