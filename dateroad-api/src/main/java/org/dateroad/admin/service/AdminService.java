package org.dateroad.admin.service;

import lombok.RequiredArgsConstructor;
import org.dateroad.admin.domain.Warning;
import org.dateroad.admin.dto.CourseAdminDto;
import org.dateroad.admin.dto.CourseFilterReq;
import org.dateroad.admin.dto.response.AdminUserResponse;
import org.dateroad.admin.repository.WarningRepository;
import org.dateroad.code.FailureCode;
import org.dateroad.course.dto.response.CourseResponse;
import org.dateroad.date.domain.Course;

import org.dateroad.date.repository.CourseRepository;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.like.domain.Like;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final WarningRepository warningRepository;

    public Page<User> getAllUsers(Pageable pageable, Boolean active) {
        if(!active){
            return userRepository.findAll(pageable);
        }
        String activeUserFilter = "USER DELETED";
        return userRepository.findAllByPlatformUserIdNotContaining(pageable, activeUserFilter);
    }

    // 사용자 상세 정보 조회
    public AdminUserResponse getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
        return AdminUserResponse.from(user);
    }

    // 사용자의 코스 목록 조회
    public List<CourseResponse> getUserCourses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
        
        List<Course> courses = courseRepository.findByUser(user);
        return courses.stream()
                .map(CourseResponse::from)
                .collect(Collectors.toList());
    }

    public Page<CourseAdminDto> getAllCourses(String search, Pageable pageable, CourseFilterReq courseFilterReq) {
        // 기본 정렬 조건 설정
        if (courseFilterReq != null) {
            if (Boolean.TRUE.equals(courseFilterReq.getLatest())) {
                pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
                );
            }
        }

        Specification<Course> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 기본 조건: deleted = false
            predicates.add(cb.equal(root.get("deleted"), false));
            
            if (StringUtils.hasText(search)) {
                // 검색 조건 추가
                Predicate searchPredicate = cb.or(
                    cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.join("user").get("name")), "%" + search.toLowerCase() + "%")
                );
                predicates.add(searchPredicate);
            }

            // 인기순 정렬
            if (courseFilterReq != null && Boolean.TRUE.equals(courseFilterReq.getPopular())) {
                Join<Course, Like> likeJoin = root.join("likes", JoinType.LEFT);
                query.groupBy(root.get("id"));
                query.orderBy(cb.desc(cb.count(likeJoin)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return courseRepository.findAll(spec, pageable).map(CourseAdminDto::from);
    }

    @Transactional
    public void warnUser(Long userId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
        
        Warning warning = Warning.create(user, reason);
        warningRepository.save(warning);

        //TODO !
//        // 경고 횟수 증가 및 상태 변경
//        user.increaseWarningCount();
//        if (user.getWarningCount() >= 3) {
//            user.updateStatus(UserStatus.BANNED);
//        }
    }

    @Transactional
    public void deactivateWarning(Long warningId) {
        Warning warning = warningRepository.findById(warningId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.ENTITY_NOT_FOUND));
        warning.deactivate();
    }

    public long getActiveWarningCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
        return warningRepository.countByUserAndIsActiveTrue(user);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.COURSE_NOT_FOUND));
        courseRepository.delete(course);
    }
} 