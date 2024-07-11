package org.dateroad.date.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.tag.domain.DateTag;
import org.dateroad.tag.domain.DateTagType;

@Builder(access = AccessLevel.PRIVATE)
public record TagGetRes(
        DateTagType tag
) {
    public static TagGetRes of(DateTag dateTag) {
        return TagGetRes.builder()
                .tag(dateTag.getDateTagType())
                .build();
    }
}
