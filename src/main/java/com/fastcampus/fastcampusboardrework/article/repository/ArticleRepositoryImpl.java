package com.fastcampus.fastcampusboardrework.article.repository;

import com.fastcampus.fastcampusboardrework.article.controller.SearchType;
import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.domain.QArticle;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.fastcampus.fastcampusboardrework.article.domain.QArticle.article;
import static com.fastcampus.fastcampusboardrework.useraccount.domain.QUserAccount.userAccount;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Page<Article> findArticlePageBySearchParam(SearchType searchType, String searchKeyword, Pageable pageable) {
        BooleanExpression keywordContains = getKeywordContains(searchType, searchKeyword);

        List<Article> content = query
                .selectFrom(article)
                .leftJoin(article.userAccount, userAccount).fetchJoin()
                .where(keywordContains)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifiers(pageable.getSort()))
                .fetch();

        Long total = query
                .select(article.count())
                .from(article)
                .where(keywordContains)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression getKeywordContains(SearchType searchType, String searchKeyword) {
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            return null;
        }

        return switch (searchType) {
            case TITLE -> article.title.containsIgnoreCase(searchKeyword);
            case CONTENT -> article.content.containsIgnoreCase(searchKeyword);
            case ID -> article.userAccount.userId.eq(searchKeyword);
            case NICKNAME -> article.userAccount.nickname.eq(searchKeyword); // TODO left join 페치조인에는 별칭을 걸면 안되는데,,
            case HASHTAG -> article.hashtag.eq(searchKeyword);
        };
    }

    private OrderSpecifier<?>[] createOrderSpecifiers(Sort sort) {
        return sort.stream()
                .map(order -> new OrderSpecifier(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        new PathBuilder<>(QArticle.class, article.getMetadata().getName()).get(order.getProperty())
                ))
                .toArray(OrderSpecifier[]::new);
    }

    @Override
    public Page<Article> findByHashtag(String hashtag, Pageable pageable) {
        List<Article> content = query
                .selectFrom(article)
                .leftJoin(article.userAccount, userAccount).fetchJoin()
                .where(hashtagContains(hashtag))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifiers(pageable.getSort()))
                .fetch();

        Long total = query
                .select(article.count())
                .from(article)
                .leftJoin(article.userAccount, userAccount)
                .where(hashtagContains(hashtag))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression hashtagContains(String hashtag) {
        if (hashtag == null || hashtag.isEmpty()) {
            return null;
        }
        return article.hashtag.eq(hashtag);
    }

    @Override
    public List<String> findAllHashtagLimit100() {
        return query
                .selectDistinct(article.hashtag)
                .from(article)
                .limit(100)
                .where(article.hashtag.isNotNull())
                .groupBy(article.hashtag)
                .orderBy(article.hashtag.count().desc())
                .fetch();
    }
}
