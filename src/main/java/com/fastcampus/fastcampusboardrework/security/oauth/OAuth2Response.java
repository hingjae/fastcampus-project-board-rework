package com.fastcampus.fastcampusboardrework.security.oauth;

import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;

public interface OAuth2Response {
    UserAccount toEntity(String password);

    String username();
}
