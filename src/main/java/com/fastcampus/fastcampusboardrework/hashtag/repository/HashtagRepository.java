package com.fastcampus.fastcampusboardrework.hashtag.repository;

import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    boolean existsByHashtagName(String hashtagName);
}
