package com.fastcampus.fastcampusboardrework.article.service.dto;

import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import lombok.Builder;

public record UserAccountDto(
        String userId,
        String userPassword,
        String email,
        String nickname,
        String memo
) {

    @Builder
    public UserAccountDto {
    }

    public UserAccount toEntity() {
        return UserAccount.builder()
                .userId(userId)
                .userPassword(userPassword)
                .email(email)
                .nickname(nickname)
                .memo(memo)
                .build();
    }

    public static UserAccountDto from(UserAccount userAccount) {
        return UserAccountDto.builder()
                .userId(userAccount.getUserId())
                .userPassword(userAccount.getUserPassword())
                .email(userAccount.getEmail())
                .nickname(userAccount.getNickname())
                .memo(userAccount.getMemo())
                .build();
    }
}
