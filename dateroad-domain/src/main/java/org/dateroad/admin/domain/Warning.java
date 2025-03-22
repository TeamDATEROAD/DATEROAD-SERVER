package org.dateroad.admin.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dateroad.common.BaseTimeEntity;
import org.dateroad.user.domain.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "warnings")
public class Warning extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warning_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Column(name = "reason")
    @NotNull
    private String reason;

    @Column(name = "is_active")
    @NotNull
    private Boolean isActive;

    public static Warning create(final User user, final String reason) {
        return Warning.builder()
                .user(user)
                .reason(reason)
                .isActive(true)
                .build();
    }

    public void deactivate() {
        this.isActive = false;
    }
} 