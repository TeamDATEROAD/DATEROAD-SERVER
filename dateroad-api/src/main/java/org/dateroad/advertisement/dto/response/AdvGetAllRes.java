package org.dateroad.advertisement.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.advertisement.domain.AdTagType;
import org.dateroad.advertisement.domain.Advertisement;

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
            String title,
            AdTagType tag
    ) {
        public static AdvertismentDtoRes of(Advertisement advertisement) {
            return AdvertismentDtoRes.builder()
                    .advertismentId(advertisement.getId())
                    .thumbnail(advertisement.getThumbnail())
                    .title(advertisement.getTitle())
                    .tag(advertisement.getTag())
                    .build();
        }
    }
}
