package org.dateroad.course.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoursePlaceGetReqV2 {
    private String title;
    private float duration;
    private int sequence;
    private String address;

    public static CoursePlaceGetReqV2 of(final String title, final float duration, final int sequence, final String address) {
        return CoursePlaceGetReqV2.builder()
                .title(title)
                .duration(duration)
                .sequence(sequence)
                .address(address)
                .build();
    }
}