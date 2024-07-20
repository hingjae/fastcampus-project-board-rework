package com.fastcampus.fastcampusboardrework.security.oauth.google;

import com.fastcampus.fastcampusboardrework.security.oauth.OAuth2ResponseHandler;
import com.fastcampus.fastcampusboardrework.security.oauth.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class GoogleOAuth2ResponseHandler implements OAuth2ResponseHandler {
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User handle(OAuth2User oAuth2User) {
        GoogleOAuth2Response googleResponse = GoogleOAuth2Response.from(oAuth2User.getAttributes());

        return userAccountService.findByIdOrSave(googleResponse, passwordEncoder);
    }

    @Override
    public boolean supports(String registrationId) {
        return "google".equals(registrationId);
    }
}
