package com.fastcampus.fastcampusboardrework.security;

import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record CustomUserDetails(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        String email,
        String nickname,
        String memo,
        Map<String, Object> oAuth2attributes
) implements UserDetails, OAuth2User {

    @Builder
    public CustomUserDetails {
    }

    public static CustomUserDetails from(UserAccount userAccount) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return CustomUserDetails.builder()
                .username(userAccount.getUserId())
                .password(userAccount.getUserPassword())
                .authorities(roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet())
                )
                .email(userAccount.getEmail())
                .nickname(userAccount.getNickname())
                .memo(userAccount.getMemo())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2attributes;
    }

    @Override
    public String getName() {
        return username;
    }

    public String nickname() {
        return nickname;
    }
}
