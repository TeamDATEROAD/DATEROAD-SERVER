package org.dateroad.date.dto.request;

import org.dateroad.tag.domain.DateTagType;

public record TagCreateReq(
        DateTagType tag
) {
}
