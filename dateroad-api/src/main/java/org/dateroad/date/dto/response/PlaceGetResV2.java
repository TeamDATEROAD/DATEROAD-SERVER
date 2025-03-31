package org.dateroad.date.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.place.domain.DatePlace;

@Builder(access = AccessLevel.PRIVATE)

public record PlaceGetResV2(
        String title,
        float duration,
        int sequence,
        String address
) {
    public static PlaceGetResV2 of(DatePlace datePlace) {
        return PlaceGetResV2.builder()
                .title(datePlace.getName())
                .duration(datePlace.getDuration())
                .sequence(datePlace.getSequence())
                .address(datePlace.getAddress())
                .build();
    }
}
