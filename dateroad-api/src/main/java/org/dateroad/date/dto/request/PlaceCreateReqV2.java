package org.dateroad.date.dto.request;

public record PlaceCreateReqV2(
        String title,
        float duration,
        int sequence,
        String address
) {
}
