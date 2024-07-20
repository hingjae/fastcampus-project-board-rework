package com.fastcampus.fastcampusboardrework.security.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

@RequiredArgsConstructor
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final List<OAuth2ResponseHandler> responseHandlers;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2ResponseHandler oAuth2Handler = responseHandlers.stream()
                .filter(handler -> handler.supports(registrationId))
                .findFirst()
                .orElseThrow(() -> new OAuth2AuthenticationException("No handler found for provider: " + registrationId));

        return oAuth2Handler.handle(oAuth2User);
    }
}
