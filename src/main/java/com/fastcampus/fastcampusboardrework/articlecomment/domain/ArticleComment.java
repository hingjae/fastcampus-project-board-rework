package com.fastcampus.fastcampusboardrework.articlecomment.domain;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.domain.exception.UserNotAuthorizedException;
import com.fastcampus.fastcampusboardrework.common.config.BaseEntity;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ArticleComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id")
    private Article article;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount; // 유저 정보 (ID)

    @Setter
    @Column(length = 1000)
    private String content;

    @Builder
    public ArticleComment(Long id, Article article, UserAccount userAccount, String content) {
        this.id = id;
        this.article = article;
        this.userAccount = userAccount;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public void validateUserAccount(UserAccount userAccount) {
        if (!this.belongsTo(userAccount)) {
            throw new UserNotAuthorizedException();
        }
    }

    private boolean belongsTo(UserAccount userAccount) {
        return this.userAccount.equals(userAccount);
    }

    public void modify(String content) {
        this.content = content;
    }
}
