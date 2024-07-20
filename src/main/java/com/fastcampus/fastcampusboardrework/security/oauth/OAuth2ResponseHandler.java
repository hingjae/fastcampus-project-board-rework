package com.fastcampus.fastcampusboardrework.security.oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2ResponseHandler {
    OAuth2User handle(OAuth2User oAuth2User);

    boolean supports(String registrationId);
}
