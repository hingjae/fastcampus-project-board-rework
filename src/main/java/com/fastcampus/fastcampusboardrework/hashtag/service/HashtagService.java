package com.fastcampus.fastcampusboardrework.hashtag.service;

import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import com.fastcampus.fastcampusboardrework.hashtag.repository.HashtagRepository;
import com.fastcampus.fastcampusboardrework.hashtag.service.dto.HashtagSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    @Transactional
    public Set<Hashtag> createByHashtagNames(Set<String> hashtagNames) {
        HashtagSet existsHashtags = HashtagSet.of(findHashtagsByNames(hashtagNames));

        List<Hashtag> newHashtags = existsHashtags.getNewHashtag(hashtagNames);

        List<Hashtag> hashtags = hashtagRepository.saveAll(newHashtags);

        return existsHashtags.addHashtags(hashtags);
    }

    private List<Hashtag> findHashtagsByNames(Set<String> hashtagNames) {
        return hashtagRepository.findByHashtagNameIn(hashtagNames);
    }
}
