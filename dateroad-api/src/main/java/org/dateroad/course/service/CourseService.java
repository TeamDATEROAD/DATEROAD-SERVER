package org.dateroad.course.service;

import static org.dateroad.common.ValidatorUtil.validateUserAndCourse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.common.Constants;
import org.dateroad.course.dto.request.CourseCreateEvent;
import org.dateroad.course.dto.request.CourseCreateReq;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.course.dto.request.PointUseReq;
import org.dateroad.course.dto.request.TagCreateReq;
import org.dateroad.course.dto.response.CourseAccessGetAllRes;
import org.dateroad.course.dto.response.CourseCreateRes;
import org.dateroad.course.dto.response.CourseDtoGetRes;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.dto.response.DateAccessCreateRes;
import org.dateroad.date.domain.Course;
import org.dateroad.date.dto.response.CourseGetDetailRes;
import org.dateroad.date.repository.CourseRepository;
import org.dateroad.dateAccess.domain.DateAccess;
import org.dateroad.dateAccess.repository.DateAccessRepository;
import org.dateroad.exception.ConflictException;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.exception.ForbiddenException;
import org.dateroad.image.domain.Image;
import org.dateroad.image.repository.ImageRepository;
import org.dateroad.like.domain.Like;
import org.dateroad.like.repository.LikeRepository;
import org.dateroad.place.domain.CoursePlace;
import org.dateroad.place.repository.CoursePlaceRepository;
import org.dateroad.point.domain.TransactionType;
import org.dateroad.tag.domain.CourseTag;
import org.dateroad.tag.repository.CourseTagRepository;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
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
    private final ImageRepository imageRepository;
    private final CoursePlaceRepository coursePlaceRepository;
    private final CourseTagRepository courseTagRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CourseRollbackService courseRollbackService;

    @Cacheable(value = "courses", key = "#courseGetAllReq")
    public CourseGetAllRes getAllCourses(final CourseGetAllReq courseGetAllReq) {
        Specification<Course> spec = CourseSpecifications.filterByCriteria(courseGetAllReq);
        List<Course> courses = courseRepository.findAll(spec);
        List<CourseDtoGetRes> courseDtoGetResList = convertToDtoList(courses, Function.identity());
        return CourseGetAllRes.of(courseDtoGetResList);
    }

    @Cacheable(value = "courses", key = "#sortBy")
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
    @CacheEvict(value = "courses", allEntries = true)
    public void createCourseLike(final Long userId, final Long courseId) {
        User findUser = getUser(userId);
        Course findCourse = getCourse(courseId);
        validateUserAndCourse(findUser, findCourse);
        duplicateCourseLike(findUser, findCourse);
        saveCourseLike(findUser, findCourse);
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
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
        List<Course> courses = entities.stream().map(converter).toList();
        List<Object[]> results = likeRepository.countByCourses(courses);

        Map<Course, Integer> likeCounts = results.stream()
                .collect(Collectors.toMap(
                        result -> (Course) result[0],
                        result -> ((Long) result[1]).intValue()
                ));

        return courses.stream()
                .map(course -> convertToDto(course, likeCounts.getOrDefault(course, 0)))
                .toList();
    }

    private CourseDtoGetRes convertToDto(final Course course, final int likeCount) {
        return CourseDtoGetRes.of(
                course,
                course.getThumbnail(),
                likeCount,
                course.getTime()
        );
    }

    public CourseAccessGetAllRes getMyCourses(Long userId) {
        User findUser = getUser(userId);
        List<Course> courses = courseRepository.findByUser(findUser);
        List<CourseDtoGetRes> courseDtoGetResList = convertToDtoList(courses, Function.identity());
        return CourseAccessGetAllRes.of(courseDtoGetResList);
    }

    public CourseAccessGetAllRes getAllCourseAccessCourse(final Long userId) {
        List<Course> accesses = dateAccessRepository.findCoursesByUserIdOrderByIdDesc(userId);
        List<CourseDtoGetRes> courseDtoGetResList = convertToDtoList(accesses, Function.identity());
        return CourseAccessGetAllRes.of(courseDtoGetResList);
    }

    public User getUser(final Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
    }

    private Course getCourse(final Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.COURSE_NOT_FOUND));
    }

    private void duplicateCourseLike(User findUser, Course findCourse) {
        if (likeRepository.findLikeByUserAndCourse(findUser, findCourse).isPresent()) {
            throw new ConflictException(FailureCode.DUPLICATE_COURSE_LIKE);
        }
    }

    private void saveCourseLike(User user, Course course) {
        Like like = Like.create(course, user);
        likeRepository.save(like);
    }

    private Like getLike(User findUser, Course findCourse) {
        return likeRepository.findLikeByUserAndCourse(findUser, findCourse)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.LIKE_NOT_FOUND));
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public CourseCreateRes createCourse(final Long userId, final CourseCreateReq courseRegisterReq,
                                        final List<CoursePlaceGetReq> places, final List<MultipartFile> images,
                                        List<TagCreateReq> tags) throws ExecutionException, InterruptedException {
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
        Course newcourse = courseRepository.save(course);
        eventPublisher.publishEvent(CourseCreateEvent.of(newcourse, places, tags));
        String thumbnail = asyncService.createCourseImages(images, newcourse);// 썸
        course.setThumbnail(thumbnail);
        courseRepository.save(newcourse);  // 최종적으로 썸네일을 반영하여 저장
        RecordId recordId = asyncService.publishEvenUserPoint(userId, PointUseReq.of(Constants.COURSE_CREATE_POINT, TransactionType.POINT_GAINED, "코스 등록하기"));
        Long userCourseCount = courseRepository.countByUser(user);
        courseRollbackService.rollbackCourse(recordId);
        return CourseCreateRes.of(newcourse.getId(), user.getTotalPoint() + Constants.COURSE_CREATE_POINT, userCourseCount);
    }

    @TransactionalEventListener
    public void handleCourseCreatedEvent(CourseCreateEvent event) {
        asyncService.runAsyncTasks(event);
    }

    @Transactional
    public DateAccessCreateRes openCourse(final Long userId, final Long courseId, final PointUseReq pointUseReq) {
        Course course = getCourse(courseId);
        User user = getUser(userId);
        validateUserAndCourse(user, course);
        CoursePaymentType coursePaymentType = validateUserFreeOrPoint(user, pointUseReq.getPoint());
        processCoursePayment(coursePaymentType, userId, pointUseReq);
        dateAccessRepository.save(DateAccess.create(course, user));
        Long userPurchaseCount = dateAccessRepository.countCoursesByUserId(userId);
        return calculateUserInfo(coursePaymentType, user.getTotalPoint(), user.getFree(), userPurchaseCount);
    }

    private DateAccessCreateRes calculateUserInfo(CoursePaymentType coursePaymentType, int userTotalPoint, int userFree, Long userPurchaseCount) {
        if (coursePaymentType == CoursePaymentType.FREE) {
            return DateAccessCreateRes.of(userTotalPoint, userFree-1, userPurchaseCount);
        } else if (coursePaymentType == CoursePaymentType.POINT) {
            return DateAccessCreateRes.of(userTotalPoint - Constants.COURSE_OPEN_POINT, userFree, userPurchaseCount);
        }
        return DateAccessCreateRes.of(userTotalPoint - Constants.COURSE_OPEN_POINT, userFree, userPurchaseCount);
    }

    private CoursePaymentType validateUserFreeOrPoint(final User user, final int requiredPoints) {
        if (user.getFree() > 0) {
            return CoursePaymentType.FREE; // User가 free를 갖고 있으면 true를 반환
        } else if (user.getTotalPoint() < requiredPoints) {
            throw new EntityNotFoundException(FailureCode.INSUFFICIENT_USER_POINTS);
        }
        return CoursePaymentType.POINT;
    }

    public void processCoursePayment(final CoursePaymentType coursePaymentType, final Long userId,
                                     final PointUseReq pointUseReq) {
        if (coursePaymentType == CoursePaymentType.FREE) {
            asyncService.publishEventUserFree(userId);
        } else if (coursePaymentType == CoursePaymentType.POINT) {
            asyncService.publishEvenUserPoint(userId, pointUseReq);
        }
    }

    public CourseGetDetailRes getCourseDetail(final Long userId, final Long courseId) {
        Course foundCourse = findCourseById(courseId);
        User foundUser = findUserById(userId);
        List<Image> foundImages = imageRepository.findAllByCourseId(foundCourse.getId());
        validateImage(foundImages);

        List<CourseGetDetailRes.ImagesList> images = foundImages.stream()
                .map(imageList -> CourseGetDetailRes.ImagesList.of(
                        imageList.getImageUrl(),
                        imageList.getSequence())
                ).toList();

        List<CoursePlace> foundCoursePlaces = coursePlaceRepository.findAllCoursePlacesByCourseId(foundCourse.getId(), Sort.by(Sort.Direction.ASC, "sequence"));
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

        int likesCount = likeRepository.countByCourse(foundCourse);

        boolean isCourseMine = courseRepository.existsCourseByUserAndId(foundUser, courseId);

        boolean isUserLiked = false;

        boolean isAccess = dateAccessRepository.existsDateAccessByUserIdAndCourseId(foundUser.getId(),
                foundCourse.getId());

        if (!isCourseMine) {
            isUserLiked = likeRepository.existsLikeByUserIdAndCourseId(foundUser.getId(), foundCourse.getId());
        } else {
            isAccess = true;
        }

        return CourseGetDetailRes.of(foundCourse,
                images,
                likesCount,
                places,
                tags,
                isAccess,
                foundUser,
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

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public void deleteCourse(final Long userId, final Long courseId) {
        User findUser = getUser(userId);
        Course findCourse = getCourse(courseId);
        validateCourse(findUser, findCourse);
        likeRepository.deleteByCourse(courseId);
        coursePlaceRepository.deleteByCourse(courseId);
        courseTagRepository.deleteByCourse(courseId);
        dateAccessRepository.deleteByCourse(courseId);
        imageRepository.deleteByCourse(courseId);
        courseRepository.deleteByCourse(courseId);
    }

    private void validateCourse(final User findUser, final Course findCourse) {
        if (!findUser.equals(findCourse.getUser())) {
            throw new ForbiddenException(FailureCode.COURSE_DELETE_ACCESS_DENIED);
        }
    }
}
