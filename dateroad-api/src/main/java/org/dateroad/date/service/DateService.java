package org.dateroad.date.service;

import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.date.domain.Date;
import org.dateroad.date.dto.request.DateCreateReq;
import org.dateroad.date.dto.request.PlaceCreateReq;
import org.dateroad.date.dto.request.TagCreateReq;
import org.dateroad.date.dto.response.DateDetailRes;
import org.dateroad.date.dto.response.DateGetRes;
import org.dateroad.date.dto.response.DatesGetRes;
import org.dateroad.date.dto.response.DateGetNearestRes;
import org.dateroad.date.repository.DatePlaceRepository;
import org.dateroad.date.repository.DateTagRepository;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.exception.ForbiddenException;
import org.dateroad.exception.UnauthorizedException;
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
    public void createDate(final Long userId, final DateCreateReq dateCreateReq) {
        User findUser = getUser(userId);
        Date date = saveDate(findUser, dateCreateReq);
        saveDateTag(date, dateCreateReq.tags());
        saveDatePlace(date, dateCreateReq.places());
    }

    public DatesGetRes getDates(final Long userId, final String time) {
        LocalDate currentDate = LocalDate.now();
        List<Date> dates = fetchDatesByUserIdAndTime(userId, time, currentDate);
        List<DateGetRes> dateGetResList = dates.stream()
                .map(date -> createDateGetRes(date, currentDate)).toList();
        return DatesGetRes.of(dateGetResList);
    }

    public DateDetailRes getDateDetail(final Long userId, final Long dateId) {
        LocalDate currentDate = LocalDate.now();
        User findUser = getUser(userId);
        Date findDate = getDate(dateId);
        validateDate(findUser, findDate);
        List<DateTag> findDateTags = getDateTag(findDate);
        List<DatePlace> findDatePlaces = getDatePlace(findDate);
        int dDay = calculateDDay(findDate.getDate(), currentDate);
        return DateDetailRes.of(findDate, findDateTags, findDatePlaces, dDay);
    }

    @Transactional
    public void deleteDate(final Long userId, final Long dateId) {
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
        Date nearestDate = findNearestDate(findUser.getId(), currentDate, currentTime);
        int dDay = calculateDDay(nearestDate.getDate(), currentDate);
        return DateGetNearestRes.of(nearestDate, dDay);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
    }

    private Date saveDate(final User findUser, final DateCreateReq dateCreateReq) {
        Date date = Date.create(findUser, dateCreateReq.title(), dateCreateReq.date(),
                dateCreateReq.startAt(), dateCreateReq.country(), dateCreateReq.city());
        return dateRepository.save(date);
    }

    private void saveDateTag(final Date date, final List<TagCreateReq> tags) {
        List<DateTag> dateTags = tags.stream()
                .map(t -> DateTag.create(date, t.tag())).toList();
        dateTagRepository.saveAll(dateTags);
    }

    private void saveDatePlace(final Date date, final List<PlaceCreateReq> places) {
        List<DatePlace> datePlaces = places.stream()
                        .map(p -> DatePlace.create(date, p.title(), p.duration(), p.sequence())).toList();
        datePlaceRepository.saveAll(datePlaces);
    }

    private List<Date> fetchDatesByUserIdAndTime(final Long userId, final String time, final LocalDate currentDate) {
        if (time.equalsIgnoreCase("PAST")) {
            return dateRepository.findPastDatesByUserId(userId, currentDate);
        }
        else if (time.equalsIgnoreCase("FUTURE")) {
            return dateRepository.findFutureDatesByUserId(userId, currentDate);
        }
        else {
            throw new UnauthorizedException(FailureCode.INVALID_DATE_GET_TYPE);
        }
    }

    private DateGetRes createDateGetRes(final Date date, final LocalDate currentDate) {
        int dDay = calculateDday(date.getDate(), currentDate);
        List<DateTag> dateTags = getDateTag(date);
        return DateGetRes.of(date, dateTags, dDay);
    }

    private int calculateDday(final LocalDate date, final LocalDate currentDate) {
        return (int) ChronoUnit.DAYS.between(currentDate, date);
    }

    private Date getDate(final Long dateId) {
        return dateRepository.findById(dateId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.DATE_NOT_FOUND));
    }

    private void validateDate(final User findUser, final Date findDate) {
        if (!findUser.equals(findDate.getUser())) {
            throw new ForbiddenException(FailureCode.DATE_DELETE_ACCESS_DENIED);
        }
    }

    private List<DateTag> getDateTag(final Date date) {
        List<DateTag> dateTags = dateTagRepository.findByDate(date);
        if (dateTags == null | dateTags.isEmpty()) {
            throw new EntityNotFoundException(FailureCode.DATE_TAG_NOT_FOUND);
        }
        return dateTags;
    }

    private List<DatePlace> getDatePlace(final Date date) {
        List<DatePlace> datePlaces = datePlaceRepository.findByDate(date);
        if (datePlaces == null | datePlaces.isEmpty()) {
            throw new EntityNotFoundException(FailureCode.DATE_PLACE_NOT_FOUND);
        }
        return datePlaces;
    }
    //dDay 계산
    private int calculateDDay(LocalDate eventDate, LocalDate currentDate) {
        if (eventDate.isEqual(currentDate)) {
            return 0;
        } else {
            return (int) ChronoUnit.DAYS.between(currentDate, eventDate);
        }
    }

    private Date findNearestDate(Long userId, LocalDate currentDate, LocalTime currentTime) {
        return dateRepository.findFirstByUserIdAndDateAfterOrDateAndStartAtAfterOrderByDateAscStartAtAsc(userId, currentDate, currentDate, currentTime)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.DATE_NOT_FOUND));
    }

}
