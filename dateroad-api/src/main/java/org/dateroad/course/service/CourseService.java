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
import org.dateroad.course.dto.request.PointUseReq;
import org.dateroad.course.dto.response.CourseDtoGetRes;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.dto.response.DateAccessGetAllRes;
import org.dateroad.course.facade.AsyncService;
import org.dateroad.date.domain.Course;
import org.dateroad.date.repository.CourseRepository;
import org.dateroad.dateAccess.domain.DateAccess;
import org.dateroad.dateAccess.repository.DateAccessRepository;
import org.dateroad.exception.DateRoadException;
import org.dateroad.image.domain.Image;
import org.dateroad.like.repository.LikeRepository;
import org.dateroad.point.domain.Point;
import org.dateroad.point.repository.PointRepository;
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
    private final PointRepository pointRepository;

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

    public DateAccessGetAllRes getAllDataAccessCourse(final Long userId) {
        List<Course> accesses = dateAccessRepository.findCoursesByUserId(userId);
        List<CourseDtoGetRes> courseDtoGetResList = convertToDtoList(accesses, Function.identity());
        return DateAccessGetAllRes.of(courseDtoGetResList);
    }

    @Transactional
    public Course createCourse(final Long userId, final CourseCreateReq courseRegisterReq,
                               final List<CoursePlaceGetReq> places, final List<MultipartFile> images) {
        final float totalTime = places.stream()
                .map(CoursePlaceGetReq::getDuration)
                .reduce(0.0f, Float::sum);
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new DateRoadException(FailureCode.ENTITY_NOT_FOUND)
                );
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
        String thumnailUrl = imageList.getLast().getImageUrl();
        course.setThumbnail(thumnailUrl);
        return saveCourse;
    }

    @Transactional
    public void openCourse(final Long userId, final Long courseId, final PointUseReq pointUseReq) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new DateRoadException(FailureCode.USER_NOT_FOUND)
        );
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new DateRoadException(FailureCode.COURSE_NOT_FOUND)
        );
        Point point = Point.create(user, pointUseReq.point(), pointUseReq.type(), pointUseReq.description());
        CoursePaymentType coursePaymentType = validateUserFreeOrPoint(user, pointUseReq.point());
        processCoursePayment(coursePaymentType, user, point, pointUseReq);
        dateAccessRepository.save(DateAccess.create(course, user));
    }

    private CoursePaymentType validateUserFreeOrPoint(final User user, final int requiredPoints) {
        if (user.getFree() > 0) {
            return CoursePaymentType.FREE; // User가 free를 갖고 있으면 true를 반환
        } else if (user.getTotalPoint() < requiredPoints) {
            throw new DateRoadException(FailureCode.INSUFFICIENT_USER_POINTS);
        }
        return CoursePaymentType.POINT;
    }

    public void processCoursePayment(final CoursePaymentType coursePaymentType, User user, final Point point,
                                     final PointUseReq pointUseReq) {
        switch (coursePaymentType) {
            case FREE -> {
                asyncService.publishEventUserFree(user);
            }
            case POINT -> {
                pointRepository.save(point);
                asyncService.publishEvenUserPoint(user, pointUseReq);
            }
        }
    }
}
