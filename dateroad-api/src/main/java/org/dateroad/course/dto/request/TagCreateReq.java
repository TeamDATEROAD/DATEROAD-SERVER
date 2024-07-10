package org.dateroad.course.dto.request;


import lombok.Builder;
import lombok.Setter;
import org.dateroad.tag.domain.DateTagType;

@Builder
public record TagCreateReq(
        DateTagType tag
) {
    public static TagCreateReq of(DateTagType tag) {
        return TagCreateReq.builder().tag(tag).build();
    }

}