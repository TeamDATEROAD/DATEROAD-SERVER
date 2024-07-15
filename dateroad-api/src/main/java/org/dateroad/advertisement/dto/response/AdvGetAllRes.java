package org.dateroad.advertisement.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.advertisement.domain.AdTagType;
import org.dateroad.advertisement.domain.Advertisement;

@Builder(access = AccessLevel.PRIVATE)
public record AdvGetAllRes(
        List<AdvertisementDtoRes> advertisementDtoResList
) {
    public static AdvGetAllRes of(List<AdvertisementDtoRes> advertisementDtoResList) {
        return AdvGetAllRes.builder()
                .advertisementDtoResList(advertisementDtoResList)
                .build();
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record AdvertisementDtoRes(
            Long advertisementId,
            String thumbnail,
            String title,
            AdTagType tag
    ) {
        public static AdvertisementDtoRes of(Advertisement advertisement) {
            return AdvertisementDtoRes.builder()
                    .advertisementId(advertisement.getId())
                    .thumbnail(advertisement.getThumbnail())
                    .title(advertisement.getTitle())
                    .tag(advertisement.getTag())
                    .build();
        }
    }
}
