package com.fastcampus.fastcampusboardrework.hashtag.service.dto;

import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class HashtagSetTest {

    @DisplayName("List<Hash>로 HashtagSet을 생성한다.")
    @Test
    public void createHashtagSet() {
        HashtagSet hashtagSet = HashtagSet.of(getHashtags());

        assertThat(hashtagSet).isNotNull();
        assertThat(hashtagSet.items()).hasSize(5)
                .extracting(Hashtag::getHashtagName)
                .containsExactlyInAnyOrder("hashtag1", "hashtag2", "hashtag3", "hashtag4", "hashtag5");
    }

    @DisplayName("hashtagNames를 넣으면 HashtagSet에 존재하지 않는 Hashtag를 반환한다.")
    @Test
    public void getNewHashtagTest() {
        HashtagSet hashtagSet = HashtagSet.of(getHashtags());
        List<Hashtag> newHashtag = hashtagSet.getNewHashtag(Set.of("hashtag1", "hashtag2", "hashtag6", "hashtag7"));

        assertThat(newHashtag).isNotNull();
        assertThat(newHashtag).hasSize(2)
                .extracting(Hashtag::getHashtagName)
                .containsExactlyInAnyOrder("hashtag6", "hashtag7");
    }

    @DisplayName("hashtagNames를 넣으면 HashtagSet에 존재하지 않는 Hashtag를 반환한다.")
    @Test
    public void getNewHashtagTest2() {
        HashtagSet hashtagSet = HashtagSet.of(getHashtags());
        List<Hashtag> newHashtag = hashtagSet.getNewHashtag(Set.of("hashtag6", "hashtag7"));

        assertThat(newHashtag).isNotNull();
        assertThat(newHashtag).hasSize(2)
                .extracting(Hashtag::getHashtagName)
                .containsExactlyInAnyOrder("hashtag6", "hashtag7");
    }

    @DisplayName("hashtagNames가 이미 존재하면 newHashtags는 empty다.")
    @Test
    public void getNewHashtagTest3() {
        HashtagSet hashtagSet = HashtagSet.of(getHashtags());
        List<Hashtag> newHashtag = hashtagSet.getNewHashtag(Set.of("hashtag1", "hashtag2"));

        assertThat(newHashtag).isNotNull();
        assertThat(newHashtag).isEmpty();
        assertThat(newHashtag).hasSize(0);
    }

    @DisplayName("List<Hashtag>를 입력하면 HashtagSet에 모두 저장한다.")
    @Test
    public void addHashtagsTest() {
        HashtagSet hashtagSet = HashtagSet.of(getHashtags());

        Set<Hashtag> hashtags = hashtagSet.addHashtags(List.of(getHashtag("hashtag6"), getHashtag("hashtag7")));

        assertThat(hashtags).isNotNull();
        assertThat(hashtags).hasSize(7)
                .extracting(Hashtag::getHashtagName)
                .containsExactlyInAnyOrder("hashtag1", "hashtag2", "hashtag3", "hashtag4", "hashtag5", "hashtag6", "hashtag7");
    }

    private List<Hashtag> getHashtags() {
        return List.of(getHashtag("hashtag1"), getHashtag("hashtag2"), getHashtag("hashtag3"), getHashtag("hashtag4"), getHashtag("hashtag5"));
    }

    private Hashtag getHashtag(String hashtagName) {
        return Hashtag.create(hashtagName);
    }
}