package org.dateroad.course.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.dto.response.CourseGetAllRes.CourseDtoRes;
import org.dateroad.date.domain.Course;
import org.dateroad.date.repository.CourseRepository;
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

    public CourseGetAllRes getAllCourses(CourseGetAllReq courseGetAllReq) {
        Specification<Course> spec = CourseSpecifications.filterByCriteria(courseGetAllReq);
        List<Course> courses = courseRepository.findAll(spec);
        return CourseGetAllRes.of(courses.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    private CourseDtoRes convertToDto(Course course) {
        int likeCount = likeRepository.countByCourse(course);
        Image thumbnailImage = imageRepository.findFirstByCourseOrderBySequenceAsc(course);
        String thumbnailUrl = thumbnailImage != null ? thumbnailImage.getImageUrl() : null;
        int duration = coursePlaceRepository.findTotalDurationByCourseId(course.getId());

        return new CourseDtoRes(
                course.getId(),
                thumbnailUrl,
                course.getCity(),
                course.getTitle(),
                likeCount,
                course.getCost(),
                duration
        );
    }
}
