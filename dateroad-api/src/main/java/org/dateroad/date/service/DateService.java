package org.dateroad.date.service;

import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.date.domain.Date;
import org.dateroad.date.dto.request.DateCreateReq;
import org.dateroad.date.dto.request.PlaceCreateReq;
import org.dateroad.date.dto.request.TagCreateReq;
import org.dateroad.date.repository.DatePlaceRepository;
import org.dateroad.date.repository.DateTagRepository;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.place.domain.DatePlace;
import org.dateroad.tag.domain.DateTag;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DateService {
    private final DateRepository dateRepository;
    private final UserRepository userRepository;
    private final DateTagRepository dateTagRepository;
    private final DatePlaceRepository datePlaceRepository;

    @Transactional
    public void createDate(Long userId, DateCreateReq dateCreateReq) {
        User findUser = getUser(userId);
        Date date = createDate(findUser, dateCreateReq);
        createDateTag(date, dateCreateReq.tags());
        createDatePlace(date, dateCreateReq.places());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
    }

    private Date createDate(User findUser, DateCreateReq dateCreateReq) {
        Date date = Date.create(findUser, dateCreateReq.title(), dateCreateReq.date(),
                dateCreateReq.startAt(), dateCreateReq.country(), dateCreateReq.city());
        return dateRepository.save(date);
    }

    private void createDateTag(Date date, List<TagCreateReq> tags) {
        tags.forEach(t -> dateTagRepository.save(DateTag.create(date, t.tag())));
    }

    private void createDatePlace(Date date, List<PlaceCreateReq> places) {
        places.forEach(p -> datePlaceRepository.save(DatePlace.create(date, p.name(), p.duration(), p.sequence())));
    }
}