package com.fastcampus.fastcampusboardrework.articlecomment.repository;

import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    Set<ArticleComment> findByArticle_Id(Long articleId);

    void deleteByIdAndUserAccount_UserId(Long id, String userId);
}
