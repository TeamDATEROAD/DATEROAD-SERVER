package org.dateroad.advertisement.dto.response;

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
        String description
) {
    public static AdvGetDetailRes of(List<AdvImagesRes> images, String title, LocalDate createAt, String description) {
        return AdvGetDetailRes.builder()
                .images(images)
                .title(title)
                .createAt(createAt)
                .description(description)
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
