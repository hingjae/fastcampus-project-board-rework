package com.fastcampus.fastcampusboardrework.article.service;

import com.fastcampus.fastcampusboardrework.article.controller.SearchType;
import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.domain.ArticleHashtag;
import com.fastcampus.fastcampusboardrework.article.repository.ArticleRepository;
import com.fastcampus.fastcampusboardrework.article.service.dto.CreateArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ModifyArticleDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleWithCommentsDto;
import com.fastcampus.fastcampusboardrework.hashtag.domain.Hashtag;
import com.fastcampus.fastcampusboardrework.hashtag.service.HashtagService;
import com.fastcampus.fastcampusboardrework.hashtag.util.HashtagUtils;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import com.fastcampus.fastcampusboardrework.useraccount.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;
    private final HashtagService hashtagService;

    @Transactional(readOnly = true)
    public Page<ArticleDto> getArticlePage(SearchType searchType, String searchKeyword, Pageable pageable) {
        return articleRepository.findArticlePageBySearchParam(searchType, searchKeyword, pageable)
                .map(ArticleDto::from);
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        Article article = articleRepository.findByIdWithUserAccountAndArticleCommentsAndHashtags(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));

        return ArticleWithCommentsDto.from(article);
    }

    @Transactional
    public Long create(String userId, CreateArticleDto dto) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Set<String> hashtagNames = HashtagUtils.parseHashtagNames(dto.content());

        Set<Hashtag> hashtags = hashtagService.createByHashtagNames(hashtagNames);

        List<ArticleHashtag> articleHashtags = ArticleHashtag.create(hashtags);

        Article article = dto.toEntity(userAccount);

        article.addArticleHashtags(articleHashtags);

        return articleRepository.save(article)
                .getId();
    }

    @Transactional
    public void modify(Long articleId, String userId, ModifyArticleDto dto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(EntityNotFoundException::new);
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserAccount not found"));

        article.validateUserAccount(userAccount);
        article.modify(dto.title(), dto.content());

        Set<String> hashtagNames = HashtagUtils.parseHashtagNames(dto.content());

        article.clearHashtags();

        Set<Hashtag> hashtags = hashtagService.createByHashtagNames(hashtagNames);

        List<ArticleHashtag> articleHashtags = ArticleHashtag.create(hashtags);

        article.addArticleHashtags(articleHashtags);
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

    public Page<ArticleDto> searchArticlesViaHashtag(String searchValue, Pageable pageable) {
        return articleRepository.findByHashtag(searchValue, pageable)
                .map(ArticleDto::from);
    }

    public List<String> getHashtags() {
        return articleRepository.findAllHashtagLimit100();
    }

    public ModifyArticleDto getByIdWithUserAccount(Long articleId) {
        return articleRepository.findByIdWithUserAccount(articleId)
                .map(ModifyArticleDto::from)
                .orElseThrow(EntityNotFoundException::new);
    }
}
