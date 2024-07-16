package org.dateroad.image.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.date.domain.Course;
import org.dateroad.exception.BadRequestException;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.image.domain.Image;
import org.dateroad.image.repository.ImageRepository;
import org.dateroad.s3.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private final ImageRepository imageRepository;
    private final S3Service s3Service;
    @Value("${s3.bucket.path}")
    private String path;
    @Value("${cloudfront.domain}")
    private String cachePath;

    public List<Image> saveImages(final List<MultipartFile> images, final Course course) {
        AtomicInteger sequence = new AtomicInteger();
        List<Image> courseImages = images.stream()
                .map(img -> {
                    try {
                        return Image.create(
                                course,
                                cachePath + s3Service.uploadImage(path, img).get(),
                                sequence.getAndIncrement()
                        );
                    } catch (IOException | ExecutionException | InterruptedException e) {
                        throw new BadRequestException(FailureCode.BAD_REQUEST);
                    }
                }).toList();
        return imageRepository.saveAll(courseImages);
    }

    public Image findFirstByCourseOrderBySequenceAsc(Course course) {
        return imageRepository.findFirstByCourseOrderBySequenceAsc(course)
                .orElseThrow(
                        () -> new EntityNotFoundException(FailureCode.COURSE_THUMBNAIL_NOT_FOUND)
                );
    }
}
