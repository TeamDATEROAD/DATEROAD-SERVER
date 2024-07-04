package org.dateroad.dateTag.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.dateroad.date.domain.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "date_tags")
public class DateTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_tag_id")
    private Long id;

    @JoinColumn(name = "date_id", nullable = false)
    @ManyToOne
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "date_tag", nullable = false)
    private DateTagType dateTagType;

    public static DateTag of(Date date, DateTagType dateTagType) {
        return DateTag.builder()
                .date(date)
                .dateTagType(dateTagType)
                .build();
    }
}
