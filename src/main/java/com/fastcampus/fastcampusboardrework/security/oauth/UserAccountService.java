package com.fastcampus.fastcampusboardrework.security.oauth;

import com.fastcampus.fastcampusboardrework.security.CustomUserDetails;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import com.fastcampus.fastcampusboardrework.useraccount.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public CustomUserDetails findByIdOrSave(OAuth2Response oAuth2Response, PasswordEncoder passwordEncoder) {
        return userAccountRepository.findById(oAuth2Response.username())
                .map(CustomUserDetails::from)
                .orElseGet(() -> {
                    String password = passwordEncoder.encode("{bcrypt}" + UUID.randomUUID());

                    UserAccount userAccount = oAuth2Response.toEntity(password);

                    return CustomUserDetails.from(userAccountRepository.save(userAccount));
                });
    }

    public Optional<UserAccount> findByUsername(String username) {
        return userAccountRepository.findById(username);
    }
}
