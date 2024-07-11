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
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dateroad.common.BaseTimeEntity;
import org.dateroad.date.domain.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "date_tags")
public class DateTag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_tag_id")
    private Long id;

    @JoinColumn(name = "date_id")
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "date_tag")
    @NotNull
    private DateTagType dateTagType;

    public static DateTag create(final Date date, final DateTagType dateTagType) {
        return DateTag.builder()
                .date(date)
                .dateTagType(dateTagType)
                .build();
    }
}
