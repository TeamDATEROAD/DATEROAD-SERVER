package org.dateroad.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dateroad.common.BaseTimeEntity;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "platform_id")
    @NotNull
    private String platformId;

    @Column(name = "platform")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Platform platForm;

    @Builder.Default
    @Column(name = "free")
    @NotNull
    private int free = 3;

    @Builder.Default
    @Column(name = "total_point")
    @NotNull
    private int totalPoint = 0;

    public static User create(final String name, final String platformId, final Platform platForm) {
        return User.builder()
                .name(name)
                .platformId(platformId)
                .platForm(platForm)
                .build();
    }
}