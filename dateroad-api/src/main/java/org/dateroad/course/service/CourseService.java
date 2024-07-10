package org.dateroad.course.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.course.dto.request.CourseCreateReq;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.course.dto.response.CourseDtoGetRes;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.dto.response.DateAccessGetAllRes;
import org.dateroad.course.facade.CourseFacade;
import org.dateroad.date.domain.Course;
import org.dateroad.date.repository.CourseRepository;
import org.dateroad.dateAccess.repository.DateAccessRepository;
import org.dateroad.image.domain.Image;
import org.dateroad.like.repository.LikeRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository courseRepository;
    private final LikeRepository likeRepository;
    private final DateAccessRepository dateAccessRepository;
    private final CourseFacade courseFacade;

    public CourseGetAllRes getAllCourses(CourseGetAllReq courseGetAllReq) {
        Specification<Course> spec = CourseSpecifications.filterByCriteria(courseGetAllReq);
        List<Course> courses = courseRepository.findAll(spec);
        List<CourseDtoGetRes> courseDtoGetResList = convertToDtoList(courses, Function.identity());
        return CourseGetAllRes.of(courseDtoGetResList);
    }

    private <T> List<CourseDtoGetRes> convertToDtoList(List<T> entities, Function<T, Course> converter) {
        return entities.stream()
                .map(converter)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CourseDtoGetRes convertToDto(Course course) {
        int likeCount = likeRepository.countByCourse(course)
                .orElse(0);
        Image thumbnailImage = courseFacade.findFirstByCourseOrderBySequenceAsc(course);
        String thumbnailUrl = thumbnailImage != null ? thumbnailImage.getImageUrl() : null;
        float duration = courseFacade.findTotalDurationByCourseId(course.getId());
        return CourseDtoGetRes.of(
                course.getId(),
                thumbnailUrl,
                course.getCity(),
                course.getTitle(),
                likeCount,
                course.getCost(),
                duration
        );
    }

    public DateAccessGetAllRes getAllDataAccessCourse(Long userId) {
        List<Course> accesses = dateAccessRepository.findCoursesByUserId(userId);
        List<CourseDtoGetRes> courseDtoGetResList = convertToDtoList(accesses, Function.identity());
        return DateAccessGetAllRes.of(courseDtoGetResList);
    }

    @Transactional
    public String createCourse(Long userId, CourseCreateReq courseRegisterReq) {
        System.out.println(courseRegisterReq);
        final float totalTime = courseRegisterReq.places().stream()
                .map(CoursePlaceGetReq::duration) // 각 CoursePlaceGetReq 객체의 duration 값을 추출
                .reduce(0.0f, Float::sum); // 모든 duration 값을 합산

        final Course course = Course.create(
                courseRegisterReq.title(),
                courseRegisterReq.description(),
                courseRegisterReq.country(),
                courseRegisterReq.city(),
                courseRegisterReq.cost(),
                courseRegisterReq.date(),
                courseRegisterReq.startAt(),
                totalTime
        );
        courseFacade.createImage(courseRegisterReq.images(), course);
        courseFacade.createCoursePlaces(courseRegisterReq.places(), course);
        courseFacade.createCourseTags(courseRegisterReq.tags(), course);

        return courseRepository.save(course).getId().toString();
    }
}
