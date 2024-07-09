package org.dateroad.refreshtoken.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "refresh_token")
@Entity
public class RefreshToken {

    @Id
    @NotNull
    @Column(name = "refresh_token_id")
    private byte[] token;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public static RefreshToken create(final byte[] token, final Long userId, LocalDateTime expiredAt) {
        return RefreshToken.builder()
                .token(token)
                .userId(userId)
                .expiredAt(expiredAt)
                .build();
    }
}