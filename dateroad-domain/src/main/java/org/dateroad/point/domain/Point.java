package org.dateroad.point.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.dateroad.common.BaseTimeEntity;
import org.dateroad.user.domain.User;

@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "points")
public class Point extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "point", nullable = false)
    private int point;

    @Column(name = "transcation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "description", nullable = false)
    private String description;

    public static Point of(User user, int point, TransactionType transactionType, String description) {
        return Point.builder()
                .user(user)
                .point(point)
                .transactionType(transactionType)
                .description(description)
                .build();
    }
}
