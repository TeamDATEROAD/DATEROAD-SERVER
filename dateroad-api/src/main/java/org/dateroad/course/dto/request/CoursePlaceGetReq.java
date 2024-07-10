package org.dateroad.course.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Setter;

@Builder
public record CoursePlaceGetReq(
        String title,
        float duration,
        int sequence
) {
    public static CoursePlaceGetReq of(String title, float duration, int sequence) {
        return CoursePlaceGetReq.builder()
                .title(title)
                .duration(duration)
                .sequence(sequence)
                .build();
    }
}
