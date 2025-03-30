package org.dateroad.course.service;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.course.dto.request.CoursePlaceGetReqV2;
import org.dateroad.date.domain.Course;
import org.dateroad.place.domain.CoursePlace;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CoursePlaceService {
    public List<CoursePlace> createCoursePlace(final List<CoursePlaceGetReq> places, final Course course) {
        return places.stream()
                .map(placeReq -> CoursePlace.create(
                        placeReq.getTitle(),
                        placeReq.getDuration(),
                        course,
                        placeReq.getSequence()
                ))
                .toList();
    }

    public List<CoursePlace> createCoursePlaceV2(final List<CoursePlaceGetReqV2> places, final Course course) {
        return places.stream()
                .map(placeReq -> CoursePlace.createV2(
                        placeReq.getTitle(),
                        placeReq.getDuration(),
                        course,
                        placeReq.getSequence(),
                        placeReq.getAddress()
                ))
                .toList();
    }
}
