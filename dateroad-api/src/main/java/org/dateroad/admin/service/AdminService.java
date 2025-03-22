package org.dateroad.admin.service;

import lombok.RequiredArgsConstructor;
import org.dateroad.admin.domain.Warning;
import org.dateroad.admin.repository.WarningRepository;
import org.dateroad.code.FailureCode;
import org.dateroad.date.domain.Course;
import org.dateroad.date.repository.CourseRepository;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final WarningRepository warningRepository;

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<Course> getAllCourses(String search, Pageable pageable) {
        if (!StringUtils.hasText(search)) {
            return courseRepository.findAll(pageable);
        }

        Specification<Course> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 코스 제목 검색
            predicates.add(cb.like(cb.lower(root.get("title")), 
                "%" + search.toLowerCase() + "%"));
            
            // 작성자 이름 검색
            Join<Course, User> userJoin = root.join("user");
            predicates.add(cb.like(cb.lower(userJoin.get("name")), 
                "%" + search.toLowerCase() + "%"));
            
            return cb.or(predicates.toArray(new Predicate[0]));
        };

        return courseRepository.findAll(spec, pageable);
    }

    @Transactional
    public void warnUser(Long userId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
        Warning warning = Warning.create(user, reason);
        warningRepository.save(warning);
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
        return warningRepository.countByUserAndActiveTrue(user);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.COURSE_NOT_FOUND));
        courseRepository.delete(course);
    }
} 