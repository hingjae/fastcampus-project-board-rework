package com.fastcampus.fastcampusboardrework.hashtag.service;

import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import com.fastcampus.fastcampusboardrework.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    @Transactional
    public List<Hashtag> createByHashtagNames(List<String> hashtagNames) {
        return hashtagNames.stream()
                .filter(hashtagName -> !hashtagRepository.existsByHashtagName(hashtagName))
                .map(hashtagName -> {
                    Hashtag hashtag = Hashtag.create(hashtagName);
                    return hashtagRepository.save(hashtag);
                })
                .collect(Collectors.toList());
    }
}
