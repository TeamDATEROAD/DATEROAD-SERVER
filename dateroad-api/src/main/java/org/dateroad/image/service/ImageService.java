package org.dateroad.image.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.code.FailureCode;
import org.dateroad.date.domain.Course;
import org.dateroad.exception.BadRequestException;
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
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;
    private final S3Service s3Service;
    @Value("${s3.bucket.path}")
    private String path;
    @Value("${cloudfront.domain}")
    private String cachePath;

    @Transactional
    public List<Image> saveImages(final List<MultipartFile> images, final Course course) {
        AtomicInteger sequence = new AtomicInteger(1);
        List<CompletableFuture<Image>> futureImages = images.stream()
                .map(img -> CompletableFuture.supplyAsync(() -> {
                    try {
                        int count = sequence.getAndIncrement();
                        String imagePath = s3Service.uploadImage(path, img).get();
                        Image newImage = Image.create(
                                course,
                                cachePath + imagePath,
                                count);
                        return newImage;
                    } catch (IOException | ExecutionException | InterruptedException e) {
                        throw new BadRequestException(FailureCode.BAD_REQUEST);
                    }
                })).toList();
        List<Image> saveImages = futureImages.stream()
                .map(CompletableFuture::join)
                .toList();
        imageRepository.saveAll(saveImages);
        return saveImages;
    }

    public String getImageUrl(final MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        try {
            return cachePath + s3Service.uploadImage("/user", image).get();
        } catch (InterruptedException | ExecutionException | IOException e) {
            log.error(e.getMessage());
            throw new BadRequestException(FailureCode.WRONG_IMAGE_URL);
        }
    }
}
