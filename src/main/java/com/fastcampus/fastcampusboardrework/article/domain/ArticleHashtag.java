package com.fastcampus.fastcampusboardrework.article.domain;

import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ArticleHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @Builder
    public ArticleHashtag(Long id, Article article, Hashtag hashtag) {
        this.id = id;
        this.article = article;
        this.hashtag = hashtag;
    }

    public static List<ArticleHashtag> create(List<Hashtag> hashtags) {
        return hashtags.stream()
                .map(ArticleHashtag::createArticleHashtag)
                .toList();
    }

    private static ArticleHashtag createArticleHashtag(Hashtag hashtag) {
        return ArticleHashtag.builder()
                .hashtag(hashtag)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleHashtag that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
