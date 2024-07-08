package com.fastcampus.fastcampusboardrework.articlecomment.service;

import com.fastcampus.fastcampusboardrework.article.domain.Article;
import com.fastcampus.fastcampusboardrework.article.repository.ArticleRepository;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleCommentDto;
import com.fastcampus.fastcampusboardrework.article.service.dto.ArticleCommentDtos;
import com.fastcampus.fastcampusboardrework.articlecomment.domain.ArticleComment;
import com.fastcampus.fastcampusboardrework.articlecomment.repository.ArticleCommentRepository;
import com.fastcampus.fastcampusboardrework.articlecomment.service.dto.CreateArticleCommentDto;
import com.fastcampus.fastcampusboardrework.articlecomment.service.dto.ModifyArticleCommentDto;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import com.fastcampus.fastcampusboardrework.useraccount.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;
    private final ArticleRepository articleRepository;


    @Transactional(readOnly = true)
    public ArticleCommentDtos getByArticleId(Long articleId) {
        return ArticleCommentDtos.from(articleCommentRepository.findByArticle_Id(articleId));
    }

    @Transactional
    public void create(String userId, CreateArticleCommentDto dto) {
        Article article = articleRepository.findById(dto.articleId())
                .orElseThrow(EntityNotFoundException::new);
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        articleCommentRepository.save(dto.toEntity(article, userAccount));
    }

    @Transactional
    public void modify(Long articleCommentId, String userAccountId, ModifyArticleCommentDto dto) {
        ArticleComment articleComment = articleCommentRepository.getReferenceById(articleCommentId);
        UserAccount userAccount = userAccountRepository.findById(userAccountId)
                .orElseThrow(EntityNotFoundException::new);

        articleComment.validateUserAccount(userAccount);
        articleComment.modify(dto.content());
    }

    @Transactional
    public void delete(Long articleCommentId, String userId) {
        ArticleComment articleComment = articleCommentRepository.findById(articleCommentId)
                .orElseThrow(EntityNotFoundException::new);
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        articleComment.validateUserAccount(userAccount);
        articleCommentRepository.deleteByIdAndUserAccount_UserId(articleCommentId, userAccount.getUserId());
    }
}
