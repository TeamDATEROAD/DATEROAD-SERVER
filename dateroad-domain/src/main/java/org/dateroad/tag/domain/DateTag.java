package org.dateroad.tag.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.dateroad.common.BaseTimeEntity;
import org.dateroad.date.domain.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "date_tags")
public class DateTag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_tag_id")
    private Long id;

    @JoinColumn(name = "date_id", nullable = false)
    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "date_tag", nullable = false)
    @NotBlank
    private DateTagType dateTagType;

    public static DateTag of(Date date, DateTagType dateTagType) {
        return DateTag.builder()
                .date(date)
                .dateTagType(dateTagType)
                .build();
    }
}
