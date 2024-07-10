package org.dateroad.course.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.date.domain.Course;
import org.dateroad.place.domain.CoursePlace;
import org.dateroad.place.repository.CoursePlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CoursePlaceService {
    private final CoursePlaceRepository coursePlaceRepository;

    public float findTotalDurationByCourseId(final Long id) {
        return coursePlaceRepository.findTotalDurationByCourseId(id);
    }

    @Transactional
    public void createCoursePlace(final List<CoursePlaceGetReq> places, final Course course) {
        List<CoursePlace> coursePlaces = places.stream()
                .map(placeReq -> CoursePlace.create(
                        placeReq.title(),
                        placeReq.duration(),
                        course,
                        placeReq.sequence()
                ))
                .collect(Collectors.toList());
        coursePlaceRepository.saveAll(coursePlaces);
    }
}
