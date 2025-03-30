package org.dateroad.admin.api;

import lombok.RequiredArgsConstructor;
import org.dateroad.admin.dto.AdminLoginReq;
import org.dateroad.admin.dto.AdminLoginRes;
import org.dateroad.admin.dto.CourseAdminDto;
import org.dateroad.admin.dto.CourseFilterReq;
import org.dateroad.admin.dto.response.AdminUserResponse;
import org.dateroad.admin.service.AdminAuthService;
import org.dateroad.admin.service.AdminService;
import org.dateroad.course.dto.response.CourseResponse;
import org.dateroad.user.domain.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final AdminAuthService adminAuthService;

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable, @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(adminService.getAllUsers(pageable, active));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<AdminLoginRes> login(@RequestBody AdminLoginReq req) {
        return ResponseEntity.ok(adminAuthService.login(req));
    }

    @GetMapping("/users/{userId}/detail")
    public ResponseEntity<AdminUserResponse> getUserDetail(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserDetail(userId));
    }

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<List<CourseResponse>> getUserCourses(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserCourses(userId));
    }

    @GetMapping("/courses")
    public ResponseEntity<Page<CourseAdminDto>> getAllCourses(
            @RequestParam(required = false) String search,
            @ModelAttribute(value = "sort") CourseFilterReq courseFilterReq,
            Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllCourses(search, pageable, courseFilterReq));
    }

    @PostMapping("/users/{userId}/warn")
    public ResponseEntity<Void> warnUser(
            @PathVariable Long userId,
            @RequestParam String reason) {
        adminService.warnUser(userId, reason);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/warnings/{warningId}/deactivate")
    public ResponseEntity<Void> deactivateWarning(@PathVariable Long warningId) {
        adminService.deactivateWarning(warningId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}/warnings/count")
    public ResponseEntity<Long> getActiveWarningCount(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getActiveWarningCount(userId));
    }

    @DeleteMapping("/courses/{courseId}")
    @CacheEvict(value = "courses", allEntries = true)
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        adminService.deleteCourse(courseId);
        return ResponseEntity.ok().build();
    }
} 