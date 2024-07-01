package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import lombok.Builder;

public record UserAccountDto(
        Long id,
        String userId,
        String userPassword,
        String email,
        String nickname,
        String memo
) {

    @Builder
    public UserAccountDto(Long id, String userId, String userPassword, String email, String nickname, String memo) {
        this.id = id;
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
    }

    public UserAccount toEntity() {
        return UserAccount.builder()
                .id(id)
                .userId(userId)
                .userPassword(userPassword)
                .email(email)
                .nickname(nickname)
                .memo(memo)
                .build();
    }

    public static UserAccountDto from(UserAccount userAccount) {
        return UserAccountDto.builder()
                .id(userAccount.getId())
                .userId(userAccount.getUserId())
                .userPassword(userAccount.getUserPassword())
                .email(userAccount.getEmail())
                .nickname(userAccount.getNickname())
                .memo(userAccount.getMemo())
                .build();
    }
}
