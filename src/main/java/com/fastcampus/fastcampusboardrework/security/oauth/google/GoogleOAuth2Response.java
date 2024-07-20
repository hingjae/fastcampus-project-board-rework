package com.fastcampus.fastcampusboardrework.security.oauth.google;

import com.fastcampus.fastcampusboardrework.security.oauth.OAuth2Response;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import lombok.Builder;

import java.util.Map;

public record GoogleOAuth2Response(
        Map<String, Object> attributes,
        String name,
        String email
) implements OAuth2Response {
    private static final String GOOGLE_PREFIX = "google_";

    @Builder
    public GoogleOAuth2Response {
    }

    public String username() {
        return GOOGLE_PREFIX + email;
    }

    public static GoogleOAuth2Response from(Map<String, Object> attributes) {
        return GoogleOAuth2Response.builder()
                .attributes(attributes)
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .build();
    }

    public UserAccount toEntity(String password) {
        return UserAccount.builder()
                .userId(username())
                .userPassword(password)
                .nickname(name)
                .build();
    }
}
