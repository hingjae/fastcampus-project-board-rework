package com.fastcampus.fastcampusboardrework.useraccount.domain;

import com.fastcampus.fastcampusboardrework.common.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends BaseEntity implements Persistable<String> {

    @Id private String userId;
    @Setter @Column(nullable = false) private String userPassword;

    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickname;
    @Setter private String memo;

    @Builder
    public UserAccount(String userId, String userPassword, String email, String nickname, String memo, String createdBy) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId());
    }

    @Override
    public String getId() {
        return this.userId;
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
