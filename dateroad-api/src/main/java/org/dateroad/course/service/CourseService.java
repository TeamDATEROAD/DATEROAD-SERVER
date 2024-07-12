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
import org.dateroad.date.dto.response.CourseGetDetailRes;
import org.dateroad.date.repository.CourseRepository;
import org.dateroad.dateAccess.domain.DateAccess;
import org.dateroad.dateAccess.repository.DateAccessRepository;
import org.dateroad.exception.DateRoadException;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.image.domain.Image;
import org.dateroad.image.repository.ImageRepository;
import org.dateroad.exception.ConflictException;
import org.dateroad.like.domain.Like;
import org.dateroad.like.repository.LikeRepository;
import org.dateroad.point.domain.Point;
import org.dateroad.point.repository.PointRepository;
import org.dateroad.place.domain.CoursePlace;
import org.dateroad.place.repository.CoursePlaceRepository;
import org.dateroad.tag.domain.CourseTag;
import org.dateroad.tag.repository.CourseTagRepository;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ImageRepository imageRepository;
    private final CoursePlaceRepository coursePlaceRepository;
    private final CourseTagRepository courseTagRepository;


    public CourseGetAllRes getAllCourses(final CourseGetAllReq courseGetAllReq) {
        Specification<Course> spec = CourseSpecifications.filterByCriteria(courseGetAllReq);
        List<Course> courses = courseRepository.findAll(spec);
        List<CourseDtoGetRes> courseDtoGetResList = convertToDtoList(courses, Function.identity());
        return CourseGetAllRes.of(courseDtoGetResList);
    }

    public CourseGetAllRes getSortedCourses(String sortBy) {
        List<Course> courses;
        if (sortBy.equalsIgnoreCase("POPULAR")) {
            courses = getCoursesSortedByLikes();
        } else if (sortBy.equalsIgnoreCase("LATEST")) {
            courses = getCoursesSortedByLatest();
        } else {
            throw new EntityNotFoundException(FailureCode.SORT_TYPE_NOT_FOUND);
        }
        List<CourseDtoGetRes> courseDtoGetResList = convertToDtoList(courses, Function.identity());
        return CourseGetAllRes.of(courseDtoGetResList);
    }

    @Transactional
    public void createCourseLike(final Long userId, final Long courseId) {
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

    public List<Course> getCoursesSortedByLikes() {
        Pageable pageable = PageRequest.of(0, 5);
        return courseRepository.findTopCoursesByLikes(pageable);
    }

    public List<Course> getCoursesSortedByLatest() {
        Pageable pageable = PageRequest.of(0, 3);
        return courseRepository.findTopCoursesByCreatedAt(pageable);
    }

    private <T> List<CourseDtoGetRes> convertToDtoList(final List<T> entities, final Function<T, Course> converter) {
        return entities.stream()
                .map(converter)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CourseDtoGetRes convertToDto(final Course course) {
        int likeCount = likeRepository.countByCourse(course);
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

    @Transactional
    public void openCourse(final Long userId, final Long courseId, final PointUseReq pointUseReq) {
        User user = getUser(userId);
        Course course = getCourse(courseId);
        Point point = Point.create(user, pointUseReq.point(), pointUseReq.type(), pointUseReq.description());
        CoursePaymentType coursePaymentType = validateUserFreeOrPoint(user, pointUseReq.point());
        processCoursePayment(coursePaymentType, user, point, pointUseReq);
        dateAccessRepository.save(DateAccess.create(course, user));
    }

    private CoursePaymentType validateUserFreeOrPoint(final User user, final int requiredPoints) {
        if (user.getFree() > 0) {
            return CoursePaymentType.FREE; // User가 free를 갖고 있으면 true를 반환
        } else if (user.getTotalPoint() < requiredPoints) {
            throw new EntityNotFoundException(FailureCode.INSUFFICIENT_USER_POINTS);
        }
        return CoursePaymentType.POINT;
    }

    public void processCoursePayment(final CoursePaymentType coursePaymentType, final User user, final Point point,
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

    public CourseGetDetailRes getCourseDetail(final Long userId, final Long courseId) {

        //course - courseId, title, date, start_at, description, totalCost, city, totalTime
        Course foundCourse = findCourseById(courseId);

        //user - userId, free, totalPoint,
        User foundUser = findUserById(userId);

        //나머지 - images, places, tags, isAccess, likes
        List<Image> foundImages = imageRepository.findAllByCourseId(foundCourse.getId());
        validateImage(foundImages);

        List<CourseGetDetailRes.ImagesList> images = foundImages.stream()
                .map(imageList -> CourseGetDetailRes.ImagesList.of(
                        imageList.getImageUrl(),
                        imageList.getSequence())
                ).toList();

        List<CoursePlace> foundCoursePlaces = coursePlaceRepository.findAllCoursePlacesByCourseId(foundCourse.getId());
        validateCoursePlace(foundCoursePlaces);

        List<CourseGetDetailRes.Places> places = foundCoursePlaces.stream()
                .map(placesList -> CourseGetDetailRes.Places.of(
                        placesList.getSequence(),
                        placesList.getName(),
                        placesList.getDuration())
                ).toList();

        List<CourseTag> foundTags = courseTagRepository.findAllCourseTagByCourseId(foundCourse.getId());
        validateCourseTag(foundTags);

        List<CourseGetDetailRes.CourseTag> tags = foundTags.stream()
                .map(tagList -> CourseGetDetailRes.CourseTag.of(
                        tagList.getDateTagType())
                ).toList();

        boolean isAccess = dateAccessRepository.existsDateAccessByUserIdAndCourseId(foundUser.getId(), foundCourse.getId());

        int likesCount = likeRepository.countByCourse(foundCourse);

        boolean isCourseMine = courseRepository.existsByUserId(foundUser.getId());

        boolean isUserLiked = likeRepository.existsByUserIdAndCourseId(foundUser.getId(), foundCourse.getId());

        if (isCourseMine) {
            isUserLiked = false;
        }

        return CourseGetDetailRes.of(
                foundCourse.getId(),
                images,
                likesCount,
                foundCourse.getTime(),
                foundCourse.getDate(),
                foundCourse.getCity(),
                foundCourse.getTitle(),
                foundCourse.getDescription(),
                foundCourse.getStartAt(),
                places,
                foundCourse.getCost(),
                tags,
                isAccess,
                foundUser.getFree(),
                foundUser.getTotalPoint(),
                isCourseMine,
                isUserLiked
        );
    }

    private User findUserById(final Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND)
        );
    }

    private Course findCourseById(final Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(
                () -> new EntityNotFoundException(FailureCode.COURSE_NOT_FOUND)
        );
    }

    private void validateCoursePlace(final List<CoursePlace> coursePlaces) {
        if (coursePlaces.isEmpty()) {
            throw new EntityNotFoundException(FailureCode.COURSE_PLACE_NOT_FOUND);
        }
    }

    private void validateImage(final List<Image> images) {
        if (images.isEmpty()) {
            throw new EntityNotFoundException(FailureCode.IMAGE_NOT_FOUND);
        }
    }

    private void validateCourseTag(final List<CourseTag> courseTags) {
        if (courseTags.isEmpty()) {
            throw new EntityNotFoundException(FailureCode.COURSE_TAG_NOT_FOUND);
        }
    }
}
