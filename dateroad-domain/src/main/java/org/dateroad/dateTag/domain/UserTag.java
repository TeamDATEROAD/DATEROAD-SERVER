package org.dateroad.dateTag.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.dateroad.user.domain.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "user_tags")
public class UserTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_tag_id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "date_tag", nullable = false)
    private DateTagType dateTagType;

    public static UserTag of(User user, DateTagType dateTagType) {
        return UserTag.builder()
                .user(user)
                .dateTagType(dateTagType)
                .build();
    }
}
