package com.fastcampus.fastcampusboardrework.useraccount.repository;

import com.fastcampus.fastcampusboardrework.useraccount.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
