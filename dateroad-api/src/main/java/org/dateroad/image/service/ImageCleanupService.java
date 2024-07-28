package org.dateroad.image.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.image.domain.Image;
import org.dateroad.image.repository.ImageRepository;
import org.dateroad.s3.S3Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageCleanupService {
    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @Scheduled(cron = "0 0 4 * * ?")
    @Transactional
    public void findUnusedImages() throws IOException {
        String prefix = "course/";
        List<String> s3Keys = s3Service.getAllImageKeys(prefix);
        List<Image> dbImages = imageRepository.findAll();
        List<Image> imagesToDelete = new ArrayList<>();

        for (Image dbImage : dbImages) {
            if (!s3Keys.contains(dbImage.getImageUrl())) {
                imagesToDelete.add(dbImage);
            }
        }

        // DB에서 한 번에 삭제
        if (!imagesToDelete.isEmpty()) {
            imageRepository.deleteAllInBatch(imagesToDelete);
        }

        // DB에 없는 S3 키를 찾아 S3에서 삭제
        for (String s3Key : s3Keys) {
            if (!dbImages.stream().map(Image::getImageUrl).toList().contains(s3Key)) {
                deleteImageFromS3(s3Key);
            }
        }
    }

    private void deleteImageFromS3(String key) throws IOException {
        s3Service.deleteImage(key);
    }
}