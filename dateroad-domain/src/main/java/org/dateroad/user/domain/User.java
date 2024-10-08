package org.dateroad.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dateroad.common.BaseTimeEntity;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    @NotNull
    @Setter
    private String name;

    @Column(name = "platform_user_id")
    @NotNull
    @Setter
    private String platformUserId;

    @Column(name = "platform")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Platform platForm;

    @Column(name = "image_url")
    private String imageUrl;

    @Builder.Default
    @Column(name = "free")
    @NotNull
    @Setter
    private int free = 3;

    @Builder.Default
    @Column(name = "total_point")
    @NotNull
    @Setter
    private int totalPoint = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.getId());  // Compare using the unique ID
    }

    public static User create(final String name, final String platformUserId, final Platform platForm, final String imageUrl) {
        return User.builder()
                .name(name)
                .platformUserId(platformUserId)
                .platForm(platForm)
                .imageUrl(imageUrl)
                .build();
    }
}