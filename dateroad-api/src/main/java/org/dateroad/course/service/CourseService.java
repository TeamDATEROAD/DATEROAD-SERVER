package org.dateroad.course.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.course.dto.request.CourseCreateReq;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.course.dto.response.CourseDtoGetRes;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.dto.response.DateAccessGetAllRes;
import org.dateroad.course.facade.AsyncService;
import org.dateroad.date.domain.Course;
import org.dateroad.date.repository.CourseRepository;
import org.dateroad.dateAccess.repository.DateAccessRepository;
import org.dateroad.exception.ConflictException;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.image.domain.Image;
import org.dateroad.like.domain.Like;
import org.dateroad.like.repository.LikeRepository;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository courseRepository;
    private final LikeRepository likeRepository;
    private final DateAccessRepository dateAccessRepository;
    private final UserRepository userRepository;
    private final AsyncService asyncService;

    public CourseGetAllRes getAllCourses(CourseGetAllReq courseGetAllReq) {
        Specification<Course> spec = CourseSpecifications.filterByCriteria(courseGetAllReq);
        List<Course> courses = courseRepository.findAll(spec);
        List<CourseDtoGetRes> courseDtoGetResList = convertToDtoList(courses, Function.identity());
        return CourseGetAllRes.of(courseDtoGetResList);
    }

    @Transactional
    public void createCourseLike(Long userId, Long courseId) {
        User findUser = getUser(userId);
        Course findCourse = getCourse(courseId);
        validateCourseLike(findUser, findCourse);
        saveCourseLike(findUser, findCourse);
    }

    @Transactional
    public void deleteCourseLike(Long userId, Long courseId) {
        User findUser = getUser(userId);
        Course findCourse = getCourse(courseId);
        Like findLike = getLike(findUser, findCourse);
        likeRepository.delete(findLike);
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
        Image thumbnailImage = asyncService.findFirstByCourseOrderBySequenceAsc(course);
        String thumbnailUrl = thumbnailImage != null ? thumbnailImage.getImageUrl() : null;
        float duration = asyncService.findTotalDurationByCourseId(course.getId());
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

    private User getUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
    }

    private Course getCourse(final Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.COURSE_NOT_FOUND));
    }

    private void validateCourseLike(User findUser, Course findCourse) {
        if (likeRepository.findByUserAndCourse(findUser, findCourse).isPresent()) {
            throw new ConflictException(FailureCode.DUPLICATE_COURSE_LIKE);
        }
    }

    private void saveCourseLike(User user, Course course) {
        Like like = Like.create(course, user);
        likeRepository.save(like);
    }

    private Like getLike(User findUser, Course findCourse) {
        return likeRepository.findByUserAndCourse(findUser, findCourse)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.LIKE_NOT_FOUND));
    }

    @Transactional
    public Course createCourse(final Long userId, final CourseCreateReq courseRegisterReq,
                               final List<CoursePlaceGetReq> places, final List<MultipartFile> images) {
        final float totalTime = places.stream()
                .map(CoursePlaceGetReq::getDuration)
                .reduce(0.0f, Float::sum);
        User user = getUser(userId);
        Course course = Course.create(
                user,
                courseRegisterReq.getTitle(),
                courseRegisterReq.getDescription(),
                courseRegisterReq.getCountry(),
                courseRegisterReq.getCity(),
                courseRegisterReq.getCost(),
                courseRegisterReq.getDate(),
                courseRegisterReq.getStartAt(),
                totalTime
        );
        Course saveCourse = courseRepository.save(course);
        List<Image> imageList = asyncService.createImage(images, saveCourse);
        String thumbnailUrl = imageList.getLast().getImageUrl();
        course.setThumbnail(thumbnailUrl);
        return saveCourse;
    }
}
