package com.fastcampus.fastcampusboardrework.article.controller.dto;

import com.fastcampus.fastcampusboardrework.article.controller.SearchType;
import lombok.Builder;

public record SearchArticleCondition(
        SearchType searchType,
        String searchKeyword
) {
    @Builder
    public SearchArticleCondition {
    }
}
