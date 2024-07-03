package org.dateroad.advertisement.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "advertisments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Advertisment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisment_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    public static Advertisment of(String title, String description) {
        return Advertisment.builder()
                .title(title)
                .description(description)
                .build();
    }

}
