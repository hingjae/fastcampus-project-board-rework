package com.fastcampus.fastcampusboardrework.article.domain;

import com.fastcampus.fastcampusboardrework.article.domain.exception.UserNotAuthorizedException;
import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import com.fastcampus.fastcampusboardrework.common.config.BaseEntity;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserAccount userAccount; // 유저 정보 (ID)

    @Setter private String title;

    @Setter @Column(length = 1000) private String content;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    @ToString.Exclude
    private Set<ArticleComment> articleComments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArticleHashtag> articleHashtags = new LinkedHashSet<>();

    @Builder
    public Article(Long id, UserAccount userAccount, String title, String content, Set<ArticleComment> articleComments, Set<ArticleHashtag> articleHashtags) {
        this.id = id;
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.articleComments = articleComments;
        this.articleHashtags = articleHashtags;
    }

    public static Article create(UserAccount userAccount, String title, String content) {
        return Article.builder()
                .userAccount(userAccount)
                .title(title)
                .content(content)
                .articleHashtags(new LinkedHashSet<>())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validateUserAccount(UserAccount userAccount) {
        if (!this.belongsTo(userAccount)) {
            throw new UserNotAuthorizedException();
        }
    }

    private boolean belongsTo(UserAccount userAccount) {
        return this.userAccount.equals(userAccount);
    }

    public void addArticleHashtag(ArticleHashtag articleHashtag) {
        this.articleHashtags.add(articleHashtag);
        articleHashtag.setArticle(this);
    }

    public void addArticleHashtags(Collection<ArticleHashtag> articleHashtags) {
        for (ArticleHashtag articleHashtag : articleHashtags) {
            this.addArticleHashtag(articleHashtag);
        }
    }
}
