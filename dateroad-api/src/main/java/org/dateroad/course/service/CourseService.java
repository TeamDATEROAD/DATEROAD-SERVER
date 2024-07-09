package org.dateroad.course.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.course.dto.response.CourseDtoRes;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.dto.response.DateAccessGetAllRes;
import org.dateroad.date.domain.Course;
import org.dateroad.date.repository.CourseRepository;
import org.dateroad.dateAccess.repository.DataAccessRepository;
import org.dateroad.image.domain.Image;
import org.dateroad.image.repository.ImageRepository;
import org.dateroad.like.repository.LikeRepository;
import org.dateroad.place.repository.CoursePlaceRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final LikeRepository likeRepository;
    private final ImageRepository imageRepository;
    private final CoursePlaceRepository coursePlaceRepository;
    private final DataAccessRepository dataAccessRepository;

    public CourseGetAllRes getAllCourses(CourseGetAllReq courseGetAllReq) {
        Specification<Course> spec = CourseSpecifications.filterByCriteria(courseGetAllReq);
        List<Course> courses = courseRepository.findAll(spec);
        List<CourseDtoRes> courseDtoResList = convertToDtoList(courses, Function.identity());
        return CourseGetAllRes.of(courseDtoResList);
    }

    private <T> List<CourseDtoRes> convertToDtoList(List<T> entities, Function<T, Course> converter) {
        return entities.stream()
                .map(converter)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CourseDtoRes convertToDto(Course course) {
        int likeCount = likeRepository.countByCourse(course);
        Image thumbnailImage = imageRepository.findFirstByCourseOrderBySequenceAsc(course);
        String thumbnailUrl = thumbnailImage != null ? thumbnailImage.getImageUrl() : null;
        float duration = coursePlaceRepository.findTotalDurationByCourseId(course.getId());

        return CourseDtoRes.of(
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
        List<Course> accesses = dataAccessRepository.findCoursesByUserId(userId);
        List<CourseDtoRes> courseDtoResList = convertToDtoList(accesses, Function.identity());
        return DateAccessGetAllRes.of(courseDtoResList);
    }
}
