package org.dateroad.course.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CoursePlaceGetReq {
    private String title;
    private float duration;
    private int sequence;

    public static CoursePlaceGetReq of(final String title, final float duration, final int sequence) {
        return CoursePlaceGetReq.builder()
                .title(title)
                .duration(duration)
                .sequence(sequence)
                .build();
    }
}