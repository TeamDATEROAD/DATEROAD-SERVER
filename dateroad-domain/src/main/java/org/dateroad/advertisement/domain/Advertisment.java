package org.dateroad.advertisement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @NotBlank
    private String title;

    @Column(name = "description", nullable = false)
    @NotBlank
    private String description;

    public static Advertisment of(String title, String description) {
        return Advertisment.builder()
                .title(title)
                .description(description)
                .build();
    }
}
