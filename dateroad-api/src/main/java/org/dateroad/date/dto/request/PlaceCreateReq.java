package org.dateroad.date.dto.request;

public record PlaceCreateReq(
        String name,
        float duration,
        int sequence
) {
}
