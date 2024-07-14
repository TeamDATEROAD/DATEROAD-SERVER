package org.dateroad.advertisement.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.dateroad.adImage.domain.AdImage;
import org.dateroad.adImage.repository.AdImageRepository;
import org.dateroad.advertisement.domain.Advertisement;
import org.dateroad.advertisement.dto.response.AdvGetAllRes;
import org.dateroad.advertisement.repository.AdvertisementRepository;
import org.dateroad.advertisement.dto.response.AdvGetAllRes.AdvertisementDtoRes;
import org.dateroad.advertisement.dto.response.AdvGetDetailRes;
import org.dateroad.advertisement.dto.response.AdvGetDetailRes.AdvImagesRes;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdImageRepository adImageRepository;

    private static List<AdvImagesRes> getImages(final List<AdImage> adImages) {
        return adImages.stream().map(
                adImage -> AdvImagesRes.of(adImage.getImageUrl(), adImage.getSequence())
        ).toList();
    }

    public AdvGetAllRes getAlladvertisements() {
        Pageable topFive = PageRequest.of(0, 5);
        return AdvGetAllRes.of(advertisementRepository.findTop5ByOrderByCreatedDateDesc(topFive).
                stream()
                .map(AdvertisementDtoRes::of)
                .collect(Collectors.toList()));
    }

    public AdvGetDetailRes getadvertisementsDetail(final Long advId) {
        Advertisement advertisement = getadvertisement(advId);
        List<AdImage> adImages = adImageRepository.findAllById(advId);
        return AdvGetDetailRes.of(
                getImages(adImages), advertisement.getTitle(), advertisement.getCreatedAt().toLocalDate(),
                advertisement.getTitle()
        );
    }

    private Advertisement getadvertisement(final Long advId) {
        return advertisementRepository.findById(advId).orElseThrow(
                () -> new EntityNotFoundException(FailureCode.ADVERTISEMENT_NOT_FOUND)
        );
    }
}
