package com.fastcampus.fastcampusboardrework.article.repository;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    @Query("select a " +
            " from Article a " +
            " join fetch a.articleComments" +
            " join fetch a.userAccount" +
            " where a.id = :id")
    Optional<Article> findByIdWithUserAccountAndArticleComments(@Param("id") Long id);
}
