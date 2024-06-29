package com.fastcampus.fastcampusboardrework.articlecomment.repository;

import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
