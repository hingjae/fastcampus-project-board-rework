package com.fastcampus.fastcampusboardrework.security;

import com.fastcampus.fastcampusboardrework.security.oauth.UserAccountService;
import com.fastcampus.fastcampusboardrework.security.oauth.google.GoogleOAuth2ResponseHandler;
import com.fastcampus.fastcampusboardrework.security.oauth.kakao.KakaoOAuth2ResponseHandler;
import com.fastcampus.fastcampusboardrework.security.oauth.OAuth2ResponseHandler;
import com.fastcampus.fastcampusboardrework.security.oauth.OAuth2UserServiceImpl;
import com.fastcampus.fastcampusboardrework.useraccount.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserAccountService userAccountService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/articles", "/articles/search-hashtag").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .oauth2Login(oAuth2 -> oAuth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService()))
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userAccountService
                .findByUsername(username)
                .map(CustomUserDetails::from)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new OAuth2UserServiceImpl(List.of(kakaoOAuth2ResponseHandlers(), googleOAuth2ResponseHandlers()));
    }

    @Bean
    public OAuth2ResponseHandler kakaoOAuth2ResponseHandlers() {
        return new KakaoOAuth2ResponseHandler(userAccountService, passwordEncoder());
    }

    @Bean
    public OAuth2ResponseHandler googleOAuth2ResponseHandlers() {
        return new GoogleOAuth2ResponseHandler(userAccountService, passwordEncoder());
    }
}
