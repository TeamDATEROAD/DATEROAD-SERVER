package org.dateroad.date.dto.request;

public record PlaceCreateReq(
        String title,
        float duration,
        int sequence
) {
}
