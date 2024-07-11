package org.dateroad.course.dto.request;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dateroad.tag.domain.DateTagType;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TagCreateReq{
    private DateTagType tag;

    public static TagCreateReq of(DateTagType tag) {
        return TagCreateReq.builder()
                .tag(tag).build();
    }
}