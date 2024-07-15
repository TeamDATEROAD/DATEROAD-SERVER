package org.dateroad.advertisement.domain;

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

@Entity
@Getter
@Table(name = "advertisements")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Advertisement extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisement_id")
    private Long id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "thumbnail")
    @NotNull
    private String thumbnail;

    @Column(name = "tag")
    @NotNull
    @Enumerated(EnumType.STRING)
    private AdTagType tag;

    public static Advertisement create(
            final String title,
            final String description,
            final String thumbnail,
            final AdTagType tag) {
        return Advertisement.builder()
                .title(title)
                .description(description)
                .thumbnail(thumbnail)
                .tag(tag)
                .build();
    }
}
