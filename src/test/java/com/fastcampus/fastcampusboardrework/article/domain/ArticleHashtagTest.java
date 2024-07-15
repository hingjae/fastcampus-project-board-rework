package com.fastcampus.fastcampusboardrework.article.domain;

import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class ArticleHashtagTest {
    @DisplayName("Set<hashtag>로 List<ArticleHashtag>를 생성한다.")
    @Test
    public void toArticleHashtagList() {
        Hashtag hashtag1 = getHashtag(1L, "hashtag1");
        Hashtag hashtag2 = getHashtag(2L, "hashtag2");
        Set<Hashtag> hashtags = Set.of(hashtag1, hashtag2);

        List<ArticleHashtag> articleHashtags = ArticleHashtag.create(hashtags);

        Assertions.assertThat(articleHashtags).hasSize(2)
                .extracting(articleHashtag -> articleHashtag.getHashtag().getHashtagName())
                .containsExactlyInAnyOrder("hashtag1", "hashtag2");
    }

    private Hashtag getHashtag(Long id, String hashtagName) {
        return Hashtag.builder()
                .id(id)
                .hashtagName(hashtagName)
                .build();
    }
}