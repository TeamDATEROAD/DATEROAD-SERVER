package org.dateroad.image.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        List<Image> savedImages = new ArrayList<>();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<Void>> futures = IntStream.range(0, images.size())
                    .mapToObj(index -> CompletableFuture.runAsync(() -> {
                        try {
                            String imagePath = s3Service.uploadImage(path, images.get(index));
                            Image newImage = Image.create(
                                    course,
                                    cachePath + imagePath,
                                    index + 1
                            );
                            savedImages.add(newImage);
                        } catch (IOException e) {
                            throw new BadRequestException(FailureCode.BAD_REQUEST);
                        }
                    }, executor))
                    .toList();
            // Wait for all tasks to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            savedImages.sort(Comparator.comparing(Image::getSequence));
            imageRepository.saveAll(savedImages);
            executor.shutdown(); // Shutdown the ExecutorService
        }
        return savedImages;
    }


    public String getImageUrl(final MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        try {
            return cachePath + s3Service.uploadImage("/user", image);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BadRequestException(FailureCode.WRONG_IMAGE_URL);
        }
    }
}
