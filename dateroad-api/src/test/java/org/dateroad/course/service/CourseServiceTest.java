package org.dateroad.course.service;

import autoparams.AutoSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import javax.xml.validation.Validator;
import org.dateroad.code.FailureCode;
import org.dateroad.common.ValidatorUtil;
import org.dateroad.course.dto.request.CourseCreateEvent;
import org.dateroad.course.dto.request.CourseCreateReq;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.course.dto.request.PointUseReq;
import org.dateroad.course.dto.request.TagCreateReq;
import org.dateroad.course.dto.response.CourseDtoGetRes;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.service.AsyncService;
import org.dateroad.course.service.CourseService;
import org.dateroad.date.domain.Course;
import org.dateroad.date.domain.Region;
import org.dateroad.date.domain.Region.MainRegion;
import org.dateroad.date.domain.Region.SubRegion;
import org.dateroad.date.repository.CourseRepository;
import org.dateroad.dateAccess.repository.DateAccessRepository;
import org.dateroad.exception.ConflictException;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.exception.ForbiddenException;
import org.dateroad.image.repository.ImageRepository;
import org.dateroad.like.domain.Like;
import org.dateroad.like.repository.LikeRepository;
import org.dateroad.place.repository.CoursePlaceRepository;
import org.dateroad.point.domain.Point;
import org.dateroad.point.repository.PointRepository;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.tag.domain.UserTag;
import org.dateroad.tag.repository.CourseTagRepository;
import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.dateroad.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.dateroad.common.ValidatorUtil.*;
import static org.dateroad.common.ValidatorUtil.validateUserAndCourse;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AsyncService asyncService;
    @Mock
    private PointRepository pointRepository;
    @Mock
    private CourseTagRepository courseTagRepository;
    @Mock
    private DateAccessRepository dateAccessRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private UserService userService;
    @Mock
    private CoursePlaceRepository coursePlaceRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    ValidatorUtil valid;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private StreamOperations<String, String, String> streamOperations;
    @InjectMocks
    private CourseService courseService;

    @Test
    void 코스를_생성하면_각_메소드가_호출되었는지_확인한다() {
        // Given
        Long userId = 1L;
        CourseCreateReq courseCreateReq = CourseCreateReq.of(
                "Course Title",
                LocalDate.of(2024, 7, 4),
                LocalTime.of(12, 30),
                MainRegion.SEOUL,
                SubRegion.GANGNAM_SEOCHO,
                "This is a detailed description of the course.",
                100);

        List<CoursePlaceGetReq> places = Arrays.asList(
                CoursePlaceGetReq.of("Place 1", 1.5f, 1),
                CoursePlaceGetReq.of("Place 2", 2.0f, 2)
        );

        List<MultipartFile> images = Arrays.asList(
                mock(MultipartFile.class),
                mock(MultipartFile.class)
        );

        List<TagCreateReq> tags = Arrays.asList(
                TagCreateReq.of(DateTagType.ACTIVITY),
                TagCreateReq.of(DateTagType.DRIVE)
        );

        User user = mock(User.class);
        when(userRepository.findUserById(userId)).thenReturn(Optional.ofNullable(user)); // 수정된 부분
        Course course = Course.create(
                user,
                courseCreateReq.getTitle(),
                courseCreateReq.getDescription(),
                courseCreateReq.getCountry(),
                courseCreateReq.getCity(),
                courseCreateReq.getCost(),
                courseCreateReq.getDate(),
                courseCreateReq.getStartAt(),
                places.stream().map(CoursePlaceGetReq::getDuration).reduce(0.0f, Float::sum)
        );
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        String expectedThumbnailUrl = "http://example.com/thumbnail.jpg";
        when(asyncService.createCourseImages(images, course)).thenReturn(expectedThumbnailUrl);

        // When
//        Course result = courseService.createCourse(userId, courseCreateReq, places, images, tags);

        // Then
//        assertNotNull(result);
//        assertEquals(course, result);
        verify(courseRepository, times(2)).save(any(Course.class)); // Initial save and save with thumbnail
        verify(asyncService).createCourseImages(images, course);
        verify(eventPublisher).publishEvent(any(CourseCreateEvent.class));
        verify(asyncService).publishEvenUserPoint(eq(userId), any(PointUseReq.class));
    }
    @ParameterizedTest
    @AutoSource
    void 서로_다른_유저일때_코스에_좋아요_생성가능(Long userId, Long courseId, User user, User anotherUser, MainRegion country, SubRegion city, int cost,
                              LocalDate date, LocalTime startAt, float time) {
        Course course = Course.create(
                anotherUser, "title", "desc", country, city, cost, date, startAt, time);
        // given
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(likeRepository.findLikeByUserAndCourse(user, course)).thenReturn(Optional.empty());
        // when
        courseService.createCourseLike(userId, courseId);
        // then
        verify(likeRepository).save(any(Like.class));
    }

    @ParameterizedTest
    @AutoSource
    void 유저가_만든_코스에_좋아요_접근_불가능_테스트(Long userId, Long courseId, User user, MainRegion country, SubRegion city, int cost,
                                 LocalDate date, LocalTime startAt, float time) {
        Course course = Course.create(
                user, "title", "desc", country, city, cost, date, startAt, time);
        // given
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        // when
        // then
        assertThatThrownBy(() -> courseService.createCourseLike(userId, courseId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(FailureCode.FORBIDDEN.getMessage());
        // 유저 저장 및 태그 저장 호출되지 않았는지 검증
        verify(likeRepository, never()).save(any(Like.class));
    }

    @ParameterizedTest
    @AutoSource
    void 같은_유저가_코스에_좋아요_중복_불가능_테스트(Long userId, Long courseId, User user, User anotherUser,MainRegion country, SubRegion city, int cost,
                                       LocalDate date, LocalTime startAt, float time) {
        Course course = Course.create(
                anotherUser, "title", "desc", country, city, cost, date, startAt, time);
        // givenㅇㄴㄹ
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(likeRepository.findLikeByUserAndCourse(user, course)).thenReturn(Optional.of(Like.create(course, user)));
        // when
        // then
        assertThatThrownBy(() -> courseService.createCourseLike(userId, courseId))
                .isInstanceOf(ConflictException.class)
                .hasMessage(FailureCode.DUPLICATE_COURSE_LIKE.getMessage());
        // 유저 저장 및 태그 저장 호출되지 않았는지 검증
        verify(likeRepository, never()).save(any(Like.class));
    }

    @ParameterizedTest
    @AutoSource
    void 코스_삭제_테스트(Long userId, Long courseId, User user, Course course, Like like) {
        // given
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(likeRepository.findLikeByUserAndCourse(user, course)).thenReturn(Optional.of(like));
        // when
        courseService.deleteCourseLike(userId, courseId);
        // then
        verify(likeRepository).delete(like);
    }

    @ParameterizedTest
    @AutoSource
    void 코스_생성_테스트_오토파람(Long userId, CourseCreateReq req, List<CoursePlaceGetReq> places,
                                            List<TagCreateReq> tags, User user, Course course) {
        // given
        List<MultipartFile> images = List.of(mock(MultipartFile.class), mock(MultipartFile.class));  // MultipartFile을 Mock으로 처리
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        // when
//        Course result = courseService.createCourse(userId, req, places, images, tags);
        // then
//        assertNotNull(result);
        verify(asyncService).createCourseImages(images, course);
        verify(eventPublisher).publishEvent(any(CourseCreateEvent.class));
        verify(asyncService).publishEvenUserPoint(eq(userId), any(PointUseReq.class));
    }

    @ParameterizedTest
    @AutoSource
    void 코스_삭제_메소드_실행_테스트(Long userId, Long courseId, User user,MainRegion country, SubRegion city, int cost,
                          LocalDate date, LocalTime startAt, float time) {
        Course course = Course.create(
                user, "title", "desc", country, city, cost, date, startAt, time);
        // given
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        // when
        courseService.deleteCourse(userId, courseId);
        // then
        verify(courseRepository).deleteByCourse(courseId);
        verify(coursePlaceRepository).deleteByCourse(courseId);
        verify(courseTagRepository).deleteByCourse(courseId);
        verify(dateAccessRepository).deleteByCourse(courseId);
        verify(imageRepository).deleteByCourse(courseId);
    }

    @ParameterizedTest
    @AutoSource
    void 코스정렬_POPULAR_테스트(List<Course> courses) {
        // given
        when(courseRepository.findTopCoursesByLikes(any(Pageable.class))).thenReturn(courses);
        when(likeRepository.countByCourses(any())).thenReturn(new ArrayList<>()); // 좋아요 개수 처리
        // when
        CourseGetAllRes result = courseService.getSortedCourses("POPULAR");
        // then
        assertThat(result).isNotNull();
        verify(courseRepository,times(1)).findTopCoursesByLikes(any(Pageable.class)); // "POPULAR"일 때 좋아요 기준으로 정렬된 코스를 조회해야 함
        verify(likeRepository, times(1)).countByCourses(courses);
    }

    @ParameterizedTest
    @AutoSource
    void 코스정렬_LATEST(List<Course> courses) {
        // given
        when(courseRepository.findTopCoursesByCreatedAt(any(Pageable.class))).thenReturn(courses);
        when(likeRepository.countByCourses(any())).thenReturn(new ArrayList<>());

        // when
        CourseGetAllRes result = courseService.getSortedCourses("LATEST");

        // then
        assertThat(result).isNotNull();
        verify(courseRepository).findTopCoursesByCreatedAt(any(Pageable.class)); // "LATEST"일 때 최신순으로 정렬된 코스를 조회해야 함
        verify(likeRepository, times(1)).countByCourses(courses);
    }
    @ParameterizedTest
    @AutoSource
    void 정렬된코스_쿼리파람에_잘못된_값_들어왔을경우_에러반환() {
        // when & then
        assertThatThrownBy(() -> courseService.getSortedCourses("INVALID"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(FailureCode.SORT_TYPE_NOT_FOUND.getMessage());
    }

    @ParameterizedTest
    @AutoSource
    void 정렬된_코스LIKE_반환의_사이즈가_같은지_측정(List<Course> course) {
        // given
        when(courseRepository.findTopCoursesByLikes(any(Pageable.class))).thenReturn(course);
        // when
        List<Course> result = courseService.getCoursesSortedByLikes();
        // then
        assertThat(result).hasSize(course.size());
        verify(courseRepository,times(1)).findTopCoursesByLikes(any(Pageable.class));
    }
    @ParameterizedTest
    @AutoSource
    void 정렬된_코스_생성시점_반환의_사이즈가_같은지_측정(List<Course> courses) {
        // given
        when(courseRepository.findTopCoursesByCreatedAt(any(Pageable.class))).thenReturn(courses);
        // when
        List<Course> result = courseService.getCoursesSortedByLatest();
        // then
        assertThat(result).hasSize(courses.size());
        verify(courseRepository).findTopCoursesByCreatedAt(any(Pageable.class));
    }
}