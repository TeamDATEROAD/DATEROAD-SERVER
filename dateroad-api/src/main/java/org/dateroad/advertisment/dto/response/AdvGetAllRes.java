package org.dateroad.advertisment.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.advertisement.domain.Advertisment;

@Builder(access = AccessLevel.PRIVATE)
public record AdvGetAllRes(
        List<AdvertismentDtoRes> advertismentDtoResList
) {
    public static AdvGetAllRes of(List<AdvertismentDtoRes> advertismentDtoResList) {
        return AdvGetAllRes.builder()
                .advertismentDtoResList(advertismentDtoResList)
                .build();
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record AdvertismentDtoRes(
            Long advertismentId,
            String thumbnail,
            String title
    ) {
        public static AdvertismentDtoRes of(Advertisment advertisment) {
            return AdvertismentDtoRes.builder()
                    .advertismentId(advertisment.getId())
                    .thumbnail(advertisment.getThumbnail())
                    .title(advertisment.getTitle())
                    .build();
        }
    }
}
