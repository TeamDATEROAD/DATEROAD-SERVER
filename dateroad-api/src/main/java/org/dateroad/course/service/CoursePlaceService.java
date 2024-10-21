package org.dateroad.course.service;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.date.domain.Course;
import org.dateroad.place.domain.CoursePlace;
import org.dateroad.place.repository.CoursePlaceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CoursePlaceService {
    private final CoursePlaceRepository coursePlaceRepository;

    public float findTotalDurationByCourseId(final Long id) {
        return coursePlaceRepository.findTotalDurationByCourseId(id);
    }

    public void createCoursePlace(final List<CoursePlaceGetReq> places, final Course course) {
        List<CoursePlace> coursePlaces = places.stream()
                .map(placeReq -> CoursePlace.create(
                        placeReq.getTitle(),
                        placeReq.getDuration(),
                        course,
                        placeReq.getSequence()
                ))
                .toList();
        coursePlaceRepository.saveAll(coursePlaces);
    }
}
