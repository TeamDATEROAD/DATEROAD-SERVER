package org.dateroad.advertisment.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.dateroad.adImage.domain.AdImage;
import org.dateroad.adImage.repository.AdImageRepository;
import org.dateroad.advertisement.domain.Advertisment;
import org.dateroad.advertisement.repository.AdvertismentRepository;
import org.dateroad.advertisment.dto.response.AdvGetAllRes;
import org.dateroad.advertisment.dto.response.AdvGetAllRes.AdvertismentDtoRes;
import org.dateroad.advertisment.dto.response.AdvGetDetailRes;
import org.dateroad.advertisment.dto.response.AdvGetDetailRes.AdvImagesRes;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvertismentService {
    private final AdvertismentRepository advertismentRepository;
    private final AdImageRepository adImageRepository;

    private static List<AdvImagesRes> getImages(final List<AdImage> adImages) {
        return adImages.stream().map(
                adImage -> AdvImagesRes.of(adImage.getImageUrl(), adImage.getSequence())
        ).toList();
    }

    public AdvGetAllRes getAllAdvertisments() {
        Pageable topFive = PageRequest.of(0, 5);
        return AdvGetAllRes.of(advertismentRepository.findTop5ByOrderByCreatedDateDesc(topFive).
                stream()
                .map(AdvertismentDtoRes::of)
                .collect(Collectors.toList()));
    }

    public AdvGetDetailRes getAdvertismentsDetail(final Long advId) {
        Advertisment advertisment = getAdvertisment(advId);
        List<AdImage> adImages = adImageRepository.findAllById(advId);
        return AdvGetDetailRes.of(
                getImages(adImages), advertisment.getTitle(), advertisment.getCreatedAt().toLocalDate(),
                advertisment.getTitle()
        );
    }

    private Advertisment getAdvertisment(final Long advId) {
        return advertismentRepository.findById(advId).orElseThrow(
                () -> new EntityNotFoundException(FailureCode.ADVERTISMENT_NOT_FOUND)
        );
    }
}
