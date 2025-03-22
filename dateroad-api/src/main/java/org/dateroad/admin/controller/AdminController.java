package org.dateroad.admin.controller;

import lombok.RequiredArgsConstructor;
import org.dateroad.admin.dto.AdminCreateDto;
import org.dateroad.admin.dto.AdminLoginReq;
import org.dateroad.admin.dto.AdminLoginRes;
import org.dateroad.admin.service.AdminAuthService;
import org.dateroad.admin.service.AdminService;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.date.domain.Course;
import org.dateroad.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;
    private final AdminAuthService adminAuthService;

    @PostMapping
    @ResponseBody
    @RequestMapping("/asldkfjlaksjdfaasdlkfj")
    public ResponseEntity<Void> createAdmin(@RequestBody AdminCreateDto dto) {
        adminAuthService.createAdmin(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<AdminLoginRes> login(@RequestBody AdminLoginReq req) {
        return ResponseEntity.ok(adminAuthService.login(req));
    }

    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<Page<User>> getAllUsers(@UserId Long adminId, Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    @GetMapping("/courses")
    @ResponseBody
    public ResponseEntity<Page<Course>> getAllCourses(
            @UserId Long adminId,
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllCourses(search, pageable));
    }

    @PostMapping("/users/{userId}/warn")
    @ResponseBody
    public ResponseEntity<Void> warnUser(
            @UserId Long adminId,
            @PathVariable Long userId,
            @RequestParam String reason) {
        adminService.warnUser(userId, reason);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/warnings/{warningId}")
    @ResponseBody
    public ResponseEntity<Void> deactivateWarning(
            @UserId Long adminId,
            @PathVariable Long warningId) {
        adminService.deactivateWarning(warningId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}/warnings/count")
    @ResponseBody
    public ResponseEntity<Long> getActiveWarningCount(
            @UserId Long adminId,
            @PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getActiveWarningCount(userId));
    }

    @DeleteMapping("/courses/{courseId}")
    @ResponseBody
    public ResponseEntity<Void> deleteCourse(
            @UserId Long adminId,
            @PathVariable Long courseId) {
        adminService.deleteCourse(courseId);
        return ResponseEntity.ok().build();
    }
} 