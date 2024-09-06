package org.dateroad.image.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
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
        List<Image> savedImages = Collections.synchronizedList(new ArrayList<>());  // 동기화된 리스트 사용
        List<Thread> threads = IntStream.range(0, images.size())
                .mapToObj(index -> Thread.startVirtualThread(() -> {
                    try {
                        String imagePath = s3Service.uploadImage(path, images.get(index));  // S3 업로드
                        Image newImage = Image.create(
                                course,
                                cachePath + imagePath,  // 이미지 URL 생성
                                index + 1  // 입력받은 순서대로 시퀀스 부여
                        );
                        synchronized (savedImages) {
                            savedImages.add(newImage);  // 동기화된 리스트에 이미지 추가
                        }
                    } catch (IOException e) {
                        throw new BadRequestException(FailureCode.BAD_REQUEST);
                    }
                }))
                .toList();
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new BadRequestException(FailureCode.BAD_REQUEST);
            }
        }
        savedImages.sort(Comparator.comparing(Image::getSequence));
        imageRepository.saveAll(savedImages);
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
