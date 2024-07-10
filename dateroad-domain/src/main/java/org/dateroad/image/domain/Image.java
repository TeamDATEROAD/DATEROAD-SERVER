package org.dateroad.image.domain;

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
import lombok.Setter;
import org.dateroad.common.BaseTimeEntity;
import org.dateroad.date.domain.Course;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "images")
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @NotNull
    private Course course;

    @Column(name = "image_url")
    @NotNull
    @Getter
    private String imageUrl;

    @Column(name = "sequence")
    @NotNull
    private int sequence;

    public static Image create(final Course course, final String imageUrl, int sequence) {
        return Image.builder()
                .course(course)
                .imageUrl(imageUrl)
                .sequence(sequence)
                .build();
    }
}
