package com.fastcampus.fastcampusboardrework.articlecomment.repository;

import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    Set<ArticleComment> findByArticle_Id(Long articleId);

    void deleteByIdAndUserAccount_UserId(Long id, String userId);

    @Query("select ac" +
            " from ArticleComment ac" +
            " left join fetch ac.children" +
            " where ac.id = :id")
    Optional<ArticleComment> findByIdWithChildren(@Param("id") Long articleCommentId);
}
