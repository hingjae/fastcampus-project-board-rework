package com.fastcampus.fastcampusboardrework.hashtag.repository;

import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    boolean existsByHashtagName(String hashtagName);

    List<Hashtag> findByHashtagNameIn(Set<String> hashtagNames);
}
