package org.dateroad.adImage.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dateroad.advertisement.domain.Advertisment;
import org.dateroad.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "ad_images")
public class AdImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisment_id")
    @NotNull
    private Advertisment advertisment;

    @Column(name = "image_url")
    @NotNull
    private String imageUrl;

    @Column(name = "sequence")
    @NotNull
    private int sequence;

    public static AdImage create(
            final Advertisment advertisment,
            final String imageUrl,
            final int sequence
    ) {
        return AdImage.builder()
                .advertisment(advertisment)
                .imageUrl(imageUrl)
                .sequence(sequence)
                .build();
    }
}
