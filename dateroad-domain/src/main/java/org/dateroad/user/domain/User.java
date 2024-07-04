package org.dateroad.user.domain;

import jakarta.persistence.*;
import lombok.*;
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
    private String name;

    @Column(nullable = false, name = "platform_id")
    private String platformId;

    @Column(nullable = false, name = "platform")
    @Enumerated(EnumType.STRING)
    private Platform platForm;

    @Builder.Default
    @Column(nullable = false, name = "free")
    private int free = 3;

    @Builder.Default
    @Column(nullable = false, name = "total_point")
    private int totalPoint = 0;

    public static User of(String name, String platformId, Platform platForm, int free, int totalPoint) {
        return User.builder()
                .name(name)
                .platformId(platformId)
                .platForm(platForm)
                .free(3)
                .totalPoint(0)
                .build();
    }
}