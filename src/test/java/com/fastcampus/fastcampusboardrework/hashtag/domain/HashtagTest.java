package com.fastcampus.fastcampusboardrework.hashtag.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HashtagTest {

    @DisplayName("Hashtag를 생성한다.")
    @Test
    void createHashtag() {
        Hashtag hashtag = Hashtag.create("hashtag1");

        Assertions.assertThat(hashtag.getHashtagName()).isEqualTo("hashtag1");
    }
}