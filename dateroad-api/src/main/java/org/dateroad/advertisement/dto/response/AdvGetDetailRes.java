package org.dateroad.advertisement.dto.response;

import org.dateroad.advertisement.domain.AdTagType;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public record AdvGetDetailRes(
        List<AdvImagesRes> images,
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate createAt,
        String description,
        AdTagType adTagType
) {
    public static AdvGetDetailRes of(List<AdvImagesRes> images, String title, LocalDate createAt, String description, AdTagType type) {
        return AdvGetDetailRes.builder()
                .images(images)
                .title(title)
                .createAt(createAt)
                .description(description)
                .adTagType(type)
                .build();
    }

    @Builder(access = AccessLevel.PROTECTED)
    public record AdvImagesRes(
            String imagesUrl,
            int sequence
    ) {
        public static AdvImagesRes of(String imagesUrl, int sequence) {
            return AdvImagesRes.builder()
                    .imagesUrl(imagesUrl)
                    .sequence(sequence)
                    .build();
        }
    }
}
