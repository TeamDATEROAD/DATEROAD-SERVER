package org.dateroad.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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

    @Column(nullable = false, name = "name")
    @NotBlank
    private String name;

    @Column(nullable = false, name = "platform_id")
    @NotBlank
    private String platformId;

    @Column(nullable = false, name = "platform")
    @NotBlank
    @Enumerated(EnumType.STRING)
    private Platform platForm;

    @Builder.Default
    @Column(nullable = false, name = "free")
    @NotBlank
    private int free = 3;

    @Builder.Default
    @Column(nullable = false, name = "total_point")
    @NotBlank
    private int totalPoint = 0;

    public static User of(String name, String platformId, Platform platForm) {
        return User.builder()
                .name(name)
                .platformId(platformId)
                .platForm(platForm)
                .build();
    }
}