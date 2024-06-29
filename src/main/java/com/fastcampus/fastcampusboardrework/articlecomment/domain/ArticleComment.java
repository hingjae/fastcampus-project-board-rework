package com.fastcampus.fastcampusboardrework.articlecomment.domain;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ArticleComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    private String content;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime modifiedAt;

    private String modifiedBy;
}
