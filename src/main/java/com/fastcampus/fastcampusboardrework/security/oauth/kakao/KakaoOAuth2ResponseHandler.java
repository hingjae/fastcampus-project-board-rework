package com.fastcampus.fastcampusboardrework.security.oauth.kakao;

import com.fastcampus.fastcampusboardrework.security.oauth.OAuth2ResponseHandler;
import com.fastcampus.fastcampusboardrework.security.oauth.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class KakaoOAuth2ResponseHandler implements OAuth2ResponseHandler {
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User handle(OAuth2User oAuth2User) {
        KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());

        return userAccountService.findByIdOrSave(kakaoResponse, passwordEncoder);
    }

    @Override
    public boolean supports(String registrationId) {
        return "kakao".equals(registrationId);
    }
}
