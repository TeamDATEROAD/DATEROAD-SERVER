package org.dateroad.advertisement.domain;

import jakarta.persistence.*;
import lombok.*;
import org.dateroad.common.BaseTimeEntity;

@Entity
@Getter
@Table(name = "advertisments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Advertisment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisment_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    public static Advertisment of(String title, String description) {
        return Advertisment.builder()
                .title(title)
                .description(description)
                .build();
    }

}
