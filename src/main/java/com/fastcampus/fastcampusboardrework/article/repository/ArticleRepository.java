package com.fastcampus.fastcampusboardrework.article.repository;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    @Query("select a " +
            " from Article a " +
            " left join fetch a.articleComments" +
            " left join fetch a.userAccount" +
            " left join fetch a.articleHashtags" +
            " where a.id = :id")
    Optional<Article> findByIdWithUserAccountAndArticleComments(@Param("id") Long id);

    @Query("select a" +
            " from Article a" +
            " join fetch a.userAccount" +
            " where a.id = :id")
    Optional<Article> findByIdWithUserAccount(@Param("id") Long id);

    void deleteByIdAndUserAccount_UserId(Long id, String userId);
}
