package org.dateroad.date.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.place.domain.DatePlace;

@Builder(access = AccessLevel.PRIVATE)

public record PlaceGetRes(
        String name,
        float duration,
        int sequence
) {
    public static PlaceGetRes of(DatePlace datePlace) {
        return PlaceGetRes.builder()
                .name(datePlace.getName())
                .duration(datePlace.getDuration())
                .sequence(datePlace.getSequence())
                .build();
    }
}
