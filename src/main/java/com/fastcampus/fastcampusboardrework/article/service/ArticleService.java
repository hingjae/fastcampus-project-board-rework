package com.fastcampus.fastcampusboardrework.article.service;

import com.fastcampus.fastcampusboardrework.article.controller.SearchType;
import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.repository.ArticleRepository;
import com.fastcampus.fastcampusboardrework.article.service.dto.CreateArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ModifyArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleWithCommentsDto;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import com.fastcampus.fastcampusboardrework.useraccount.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> getArticlePage(SearchType searchType, String searchKeyword, Pageable pageable) {
        return articleRepository.findArticlePageBySearchParam(searchType, searchKeyword, pageable)
                .map(ArticleDto::from);
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        Article article = articleRepository.findByIdWithUserAccountAndArticleComments(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));

        return ArticleWithCommentsDto.from(article);
    }

    @Transactional
    public Long create(String userId, CreateArticleDto dto) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);
        return articleRepository.save(dto.toEntity(userAccount))
                .getId();
    }

    @Transactional
    public void modify(Long articleId, String userId, ModifyArticleDto dto) {
        Article article = articleRepository.getReferenceById(articleId);
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserAccount not found"));

        article.validateUserAccount(userAccount);
        article.modify(dto.title(), dto.content(), dto.hashtag());
    }

    @Transactional
    public void delete(Long articleId, String userId) {
        Article article = articleRepository.getReferenceById(articleId);
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserAccount not found"));

        article.validateUserAccount(userAccount);
        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userAccount.getUserId());
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    //TODO : 해시테그 고도화 후 테스트 작성 필요함.
    public Page<ArticleDto> searchArticlesViaHashtag(String searchValue, Pageable pageable) {
        return articleRepository.findByHashtag(searchValue, pageable)
                .map(ArticleDto::from);
    }

    //TODO : 해시테그 고도화 후 테스트 작성 필요함.
    public List<String> getHashtags() {
        return articleRepository.findAllHashtagLimit100();
    }

    public ArticleDto getByIdWithUserAccount(Long articleId) {
        return articleRepository.findByIdWithUserAccount(articleId)
                .map(ArticleDto::from)
                .orElseThrow(EntityNotFoundException::new);
    }
}
