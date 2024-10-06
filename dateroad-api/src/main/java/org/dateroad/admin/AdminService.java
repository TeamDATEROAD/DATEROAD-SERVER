package org.dateroad.admin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dateroad.advertisement.domain.Advertisement;
import org.dateroad.advertisement.repository.AdvertisementRepository;
import org.dateroad.date.domain.Course;
import org.dateroad.date.domain.Date;
import org.dateroad.date.repository.CourseRepository;
import org.dateroad.date.service.DateRepository;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.point.domain.Point;
import org.dateroad.point.repository.PointRepository;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CourseRepository courseRepository;

    private final DateRepository dateRepository;

    private final UserRepository userRepository;

    private final AdvertisementRepository advertisementRepository;

    private final PointRepository pointRepository;

    // Courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void updateCourse(Long id, Course updatedCourse) {
        Course course = getCourseById(id);
//        course.setTitle(updatedCourse.getTitle());
//        course.setStartAt(updatedCourse.getStartAt());
//        course.setCity(updatedCourse.getCity());
//        course.setCost(updatedCourse.getCost());
        courseRepository.save(course);
    }

    // Dates
    public List<Date> getAllDates() {
        return dateRepository.findAll();
    }

    public Date getDateById(Long id) {
        return dateRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void updateDate(Long id, Date updatedDate) {
        Date date = getDateById(id);
//        date.setTitle(updatedDate.getTitle());
//        date.setDate(updatedDate.getDate());
//        date.setStartAt(updatedDate.getStartAt());
//        date.setCity(updatedDate.getCity());
        dateRepository.save(date);
    }

    // Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void updateUser(Long id, User updatedUser) {
        User user = getUserById(id);
        user.setName(updatedUser.getName());
        userRepository.save(user);
    }

    // Advertisements
    public List<Advertisement> getAllAdvertisements() {
        return advertisementRepository.findAll();
    }

    public Advertisement getAdvertisementById(Long id) {
        return advertisementRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void updateAdvertisement(Long id, Advertisement updatedAdvertisement) {
        Advertisement advertisement = getAdvertisementById(id);
//        advertisement.setTitle(updatedAdvertisement.getTitle());
//        advertisement.setDescription(updatedAdvertisement.getDescription());
        advertisement.setThumbnail(updatedAdvertisement.getThumbnail());
//        advertisement.setTag(updatedAdvertisement.getTag());
        advertisementRepository.save(advertisement);
    }

    // Points
    public List<Point> getAllPoints() {
        return pointRepository.findAll();
    }

    public Point getPointById(Long id) {
        return pointRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void updatePoint(Long id, Point updatedPoint) {
        Point point = getPointById(id);
//        point.setPoint(updatedPoint.getPoint());
//        point.setTransactionType(updatedPoint.getTransactionType());
//        point.setDescription(updatedPoint.getDescription());
        pointRepository.save(point);
    }
}
