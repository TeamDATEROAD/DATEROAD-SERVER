package org.dateroad.date.service;

import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.date.domain.Date;
import org.dateroad.date.dto.request.DateCreateReq;
import org.dateroad.date.dto.request.PlaceCreateReq;
import org.dateroad.date.dto.request.TagCreateReq;
import org.dateroad.date.dto.response.DateGetNearestRes;
import org.dateroad.date.repository.DatePlaceRepository;
import org.dateroad.date.repository.DateTagRepository;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.exception.ForbiddenException;
import org.dateroad.place.domain.DatePlace;
import org.dateroad.tag.domain.DateTag;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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

    @Transactional
    public void deleteDate(Long userId, Long dateId) {
        User findUser = getUser(userId);
        Date findDate = getDate(dateId);
        validateDate(findUser, findDate);
        datePlaceRepository.deleteByDateId(dateId);
        dateTagRepository.deleteByDateId(dateId);
        dateRepository.deleteById(dateId);
    }

    public DateGetNearestRes getNearestDate(final Long userId) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        User findUser = getUser(userId);
        Date nearest = findNearestDate(findUser.getId(), currentDate, currentTime); ;
        int dDay = calculateDDay(nearest.getDate(), currentDate);
        return DateGetNearestRes
                .of(
                        nearest.getId(),
                        dDay,
                        nearest.getTitle(),
                        nearest.getDate().getMonthValue(),
                        nearest.getDate().getDayOfMonth(),
                        nearest.getStartAt()
                );
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
        List<DateTag> dateTags = tags.stream()
                .map(t -> DateTag.create(date, t.tag())).toList();
        dateTagRepository.saveAll(dateTags);
    }

    private void createDatePlace(Date date, List<PlaceCreateReq> places) {
        List<DatePlace> datePlaces = places.stream()
                        .map(p -> DatePlace.create(date, p.name(), p.duration(), p.sequence())).toList();
        datePlaceRepository.saveAll(datePlaces);
    }

    private Date getDate(Long dateId) {
        return dateRepository.findById(dateId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.DATE_NOT_FOUND));
    }

    private void validateDate(User findUser, Date findDate) {
        if (!findUser.equals(findDate.getUser())) {
            throw new ForbiddenException(FailureCode.DATE_DELETE_ACCESS_DENIED);
        }
    }

    //dDay 계산
    private int calculateDDay(LocalDate eventDate, LocalDate currentDate) {
        if (eventDate.isEqual(currentDate)) {
            return 0;
        } else {
            return (int) ChronoUnit.DAYS.between(currentDate, eventDate);
        }
    }

    //가장 가까운 데이트 가져오기
    private Date findNearestDate(Long userId, LocalDate currentDate, LocalTime currentTime) {
        return dateRepository.findFirstByUserIdAndDateAfterOrDateAndStartAtAfterOrderByDateAscStartAtAsc(userId, currentDate, currentDate, currentTime)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.DATE_NOT_FOUND));
    }

}
