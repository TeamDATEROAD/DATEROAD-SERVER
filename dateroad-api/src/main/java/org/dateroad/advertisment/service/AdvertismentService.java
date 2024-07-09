package org.dateroad.advertisment.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.dateroad.advertisement.repository.AdvertismentRepository;
import org.dateroad.advertisment.dto.response.AdvGetAllRes;
import org.dateroad.advertisment.dto.response.AdvGetAllRes.AdvertismentDtoRes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvertismentService {
    private final AdvertismentRepository advertismentRepository;
    public AdvGetAllRes getAllAdvertisments() {
        Pageable topFive = PageRequest.of(0, 5);
        return AdvGetAllRes.of(advertismentRepository.findTop5ByOrderByCreatedDateDesc(topFive).
                stream()
                .map(AdvertismentDtoRes::of)
                .collect(Collectors.toList()));
    }
}
