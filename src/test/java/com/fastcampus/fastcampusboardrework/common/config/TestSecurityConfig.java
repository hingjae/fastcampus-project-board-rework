package com.fastcampus.fastcampusboardrework.common.config;

import com.fastcampus.fastcampusboardrework.security.SecurityConfig;
import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import com.fastcampus.fastcampusboardrework.useraccount.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {
    // UserAccountRepository를 Mocking하여 테스트에서 사용할 수 있게 합니다.
    @MockBean
    private UserAccountRepository userAccountRepository;

    // 테스트 메서드 실행 전마다 실행될 보안 설정 메서드
    @BeforeTestMethod
    public void securitySetUp() {
        // userAccountRepository의 findById 메서드가 어떤 문자열을 인자로 받아 호출되든
        // 항상 특정 UserAccount 객체를 반환하도록 설정
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(
                UserAccount.builder()
                        .userId("userId")
                        .userPassword("pw")
                        .email("aaa@bbb.com")
                        .nickname("userNickname")
                        .memo("test memo")
                        .build()
        ));

    }
}
