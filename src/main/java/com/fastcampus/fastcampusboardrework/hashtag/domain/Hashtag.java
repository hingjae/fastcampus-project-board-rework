package com.fastcampus.fastcampusboardrework.hashtag.domain;

import com.fastcampus.fastcampusboardrework.common.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(indexes = {
        @Index(columnList = "hashtagName", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Hashtag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String hashtagName; // 해시태그 이름

    @Builder
    public Hashtag(Long id, String hashtagName) {
        this.id = id;
        this.hashtagName = hashtagName;
    }

    public static Hashtag create(String hashtagName) {
        return Hashtag.builder()
                .hashtagName(hashtagName)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hashtag that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
