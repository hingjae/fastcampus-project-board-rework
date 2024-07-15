package com.fastcampus.fastcampusboardrework.hashtag.service.dto;

import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record HashtagSet(
        Set<Hashtag> items
) {

    public static HashtagSet of(Collection<Hashtag> items) {
        return new HashtagSet(new HashSet<>(items));
    }

    public List<Hashtag> getNewHashtag(Set<String> hashtagNames) {
        Set<String> existsHashtagNames = getHashtagNames();

        return hashtagNames.stream()
                .filter(hashtagName -> !existsHashtagNames.contains(hashtagName))
                .map(Hashtag::create)
                .toList();
    }

    public Set<Hashtag> addHashtags(List<Hashtag> hashtags) {
        items.addAll(hashtags);
        return items;
    }

    private Set<String> getHashtagNames() {
        return items.stream()
                .map(Hashtag::getHashtagName)
                .collect(Collectors.toUnmodifiableSet());
    }
}
