package com.fastcampus.fastcampusboardrework.article.controller.dto.request;

import com.fastcampus.fastcampusboardrework.article.service.dto.CreateArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ModifyArticleDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SaveArticleRequestTest {
    @DisplayName("SaveArticleRequest를 CreateArticleDto로 반환한다.")
    @Test
    public void toCreateArticleDto() {
        SaveArticleRequest request = getSaveArticleRequest();

        CreateArticleDto dto = request.toCreateDto();

        assertThat(dto.title()).isEqualTo(request.title());
        assertThat(dto.content()).isEqualTo(request.content());
    }

    @DisplayName("SaveArticleRequest를 ModifyArticleDto로 반환한다.")
    @Test
    public void toModifyArticleDto() {
        SaveArticleRequest request = getSaveArticleRequest();

        ModifyArticleDto dto = request.toModifyDto();

        assertThat(dto.title()).isEqualTo(request.title());
        assertThat(dto.content()).isEqualTo(request.content());
    }

    private SaveArticleRequest getSaveArticleRequest() {
        return SaveArticleRequest.builder()
                .title("foo title")
                .content("foo content")
                .build();
    }

}