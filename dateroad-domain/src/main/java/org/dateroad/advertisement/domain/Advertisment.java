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
@Table(name = "advertisments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Advertisment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisment_id")
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


    public static Advertisment create(final String title, final String description,final String thumbnail, AdTagType tag) {
        return Advertisment.builder()
                .title(title)
                .description(description)
                .thumbnail(thumbnail)
                .tag(tag)
                .build();
    }

}
