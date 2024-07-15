package com.fastcampus.fastcampusboardrework.hashtag.service;

import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import com.fastcampus.fastcampusboardrework.hashtag.repository.HashtagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class HashtagServiceTest {

    @Autowired
    private HashtagService hashtagService;
    @Autowired
    private HashtagRepository hashtagRepository;

    @Transactional
    @DisplayName("hashtagNames를 입력하면 DB에 존재하지 않는 데이터만 저장한다.")
    @Test
    public void createByHashtagNamesTest() {
        Set<String> hashtagNames = Set.of("hashtag1", "hashtag2", "hashtag3", "hashtag4");

        Set<Hashtag> hashtags = hashtagService.createByHashtagNames(hashtagNames);

        assertThat(hashtags).hasSize(4)
                .extracting(Hashtag::getHashtagName)
                .containsExactlyInAnyOrder("hashtag1", "hashtag2", "hashtag3", "hashtag4");
    }

    @Transactional
    @DisplayName("이미 존재하는 hashtagName은 저장하지 않는다.")
    @Test
    public void createDuplicatedHashtagNameTest() {
        hashtagRepository.saveAll(List.of(getHashtag("hashtag1"), getHashtag("hashtag2"), getHashtag("hashtag3")));

        hashtagService.createByHashtagNames(Set.of("hashtag1"));

        long count = hashtagRepository.count();
        assertThat(count).isEqualTo(3);
    }

    @Transactional
    @DisplayName("hashtagNames를 입력하면 DB에 존재하지 않는 데이터만 저장한다.")
    @Test
    public void createByHashtagNamesTest2() {
        hashtagRepository.saveAll(List.of(getHashtag("hashtag1"), getHashtag("hashtag2"), getHashtag("hashtag3"), getHashtag("hashtag4")));

        Set<Hashtag> hashtags = hashtagService.createByHashtagNames(Set.of("hashtag1"));

        assertThat(hashtags).hasSize(1)
                .extracting(Hashtag::getHashtagName)
                .containsExactlyInAnyOrder("hashtag1");
    }

    @Transactional
    @DisplayName("hashtagNames를 입력하면 DB에 존재하지 않는 데이터만 저장한다.")
    @Test
    public void createByHashtagNamesTest3() {
        hashtagRepository.saveAll(List.of(getHashtag("hashtag1"), getHashtag("hashtag2"), getHashtag("hashtag3"), getHashtag("hashtag4")));

        Set<Hashtag> hashtags = hashtagService.createByHashtagNames(Set.of("hashtag1", "hashtag2", "hashtag5"));

        assertThat(hashtags).hasSize(3)
                .extracting(Hashtag::getHashtagName)
                .containsExactlyInAnyOrder("hashtag1", "hashtag2", "hashtag5");
    }

    private Hashtag getHashtag(String hashtagName) {
        return Hashtag.builder()
                .hashtagName(hashtagName)
                .build();
    }
}