package org.dateroad.admin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.dateroad.advertisement.domain.Advertisement;
import org.dateroad.date.domain.Course;
import org.dateroad.date.domain.Date;
import org.dateroad.point.domain.Point;
import org.dateroad.user.domain.User;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// AdminController.java
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final ResourceLoader resourceLoader;

    @GetMapping
    public String dashboard(Model model) throws IOException {
        model.addAttribute("courses", adminService.getAllCourses());
        model.addAttribute("dates", adminService.getAllDates());
        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("advertisements", adminService.getAllAdvertisements());
        model.addAttribute("points", adminService.getAllPoints());

        Resource resource = resourceLoader.getResource("classpath:static/img.png");
        InputStream inputStream = resource.getInputStream();
        byte[] imageBytes = inputStream.readAllBytes();
        String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
        model.addAttribute("imageBase64", imageBase64);
        return "admin/dashboard";
    }

    // Courses
    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", adminService.getAllCourses());
        return "admin/courses";
    }

    @GetMapping("/courses/edit/{id}")
    public String editCourse(@PathVariable Long id, Model model) {
        model.addAttribute("course", adminService.getCourseById(id));
        return "admin/edit-course";
    }

    @PostMapping("/courses/update/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute Course course) {
        adminService.updateCourse(id, course);
        return "redirect:/admin/courses";
    }

    // Dates
    @GetMapping("/dates")
    public String listDates(Model model) {
        model.addAttribute("dates", adminService.getAllDates());
        return "admin/dates";
    }

    @GetMapping("/dates/edit/{id}")
    public String editDate(@PathVariable Long id, Model model) {
        model.addAttribute("date", adminService.getDateById(id));
        return "admin/edit-date";
    }

    @PostMapping("/dates/update/{id}")
    public String updateDate(@PathVariable Long id, @ModelAttribute Date date) {
        adminService.updateDate(id, date);
        return "redirect:/admin/dates";
    }

    // Users
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", adminService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", adminService.getUserById(id));
        return "admin/edit-user";
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        adminService.updateUser(id, user);
        return "redirect:/admin/users";
    }

    // Advertisements
    @GetMapping("/advertisements")
    public String listAdvertisements(Model model) {
        model.addAttribute("advertisements", adminService.getAllAdvertisements());
        return "admin/advertisements";
    }

    @GetMapping("/advertisements/edit/{id}")
    public String editAdvertisement(@PathVariable Long id, Model model) {
        model.addAttribute("advertisement", adminService.getAdvertisementById(id));
        return "admin/edit-advertisement";
    }

    @PostMapping("/advertisements/update/{id}")
    public String updateAdvertisement(@PathVariable Long id, @ModelAttribute Advertisement advertisement) {
        adminService.updateAdvertisement(id, advertisement);
        return "redirect:/admin/advertisements";
    }

    // Points
    @GetMapping("/points")
    public String listPoints(Model model) {
        model.addAttribute("points", adminService.getAllPoints());
        return "admin/points";
    }

    @GetMapping("/points/edit/{id}")
    public String editPoint(@PathVariable Long id, Model model) {
        model.addAttribute("point", adminService.getPointById(id));
        return "admin/edit-point";
    }

    @PostMapping("/points/update/{id}")
    public String updatePoint(@PathVariable Long id, @ModelAttribute Point point) {
        adminService.updatePoint(id, point);
        return "redirect:/admin/points";
    }
}
