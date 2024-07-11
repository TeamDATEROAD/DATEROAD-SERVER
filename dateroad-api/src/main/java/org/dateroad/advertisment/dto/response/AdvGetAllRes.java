package org.dateroad.advertisment.dto.response;

import java.util.List;
import org.dateroad.advertisement.domain.AdTagType;
import org.dateroad.advertisement.domain.Advertisment;

public record AdvGetAllRes(
        List<AdvertismentDtoRes> advertismentDtoResList
) {
    public record AdvertismentDtoRes(
            Long advertismentId,
            String thumbnail,
            String title,
            AdTagType tag
    ){
        public static AdvertismentDtoRes of(Advertisment advertisment) {
            return new AdvertismentDtoRes(advertisment.getId(), advertisment.getTitle(), advertisment.getThumbnail(), advertisment.getTag());
        }
    }
    public static AdvGetAllRes of(List<AdvertismentDtoRes> advertismentDtoResList) {
        return new AdvGetAllRes(advertismentDtoResList);
    }
}
