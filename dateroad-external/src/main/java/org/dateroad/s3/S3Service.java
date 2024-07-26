package org.dateroad.s3;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.BadRequestException;
import org.dateroad.exception.InvalidValueException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3Service {
    private final String bucketName;
    private final AWSConfig awsConfig;
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp", "image/heic", "image/heif");
    private static final Long MAX_FILE_SIZE = 7 * 1024 * 1024L;

    public S3Service(@Value("${aws-property.s3-bucket-name}") final String bucketName, AWSConfig awsConfig) {
        this.bucketName = bucketName;
        this.awsConfig = awsConfig;
    }

    @Async
    public Future<String> uploadImage(String directoryPath, MultipartFile image) throws IOException {
        final String key = directoryPath + generateImageFileName(image);
        final S3Client s3Client = awsConfig.getS3Client();
        validateExtension(image);
        validateFileSize(image);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(image.getContentType())
                .contentDisposition("inline")
                .build();

        RequestBody requestBody = RequestBody.fromBytes(image.getBytes());
        s3Client.putObject(request, requestBody);
        return CompletableFuture.completedFuture(key);
    }

    public void deleteImage(String imageUrl) throws IOException {
        String imageKey = extractImageKeyFromImageUrl(imageUrl);
        final S3Client s3Client = awsConfig.getS3Client();

        s3Client.deleteObject((DeleteObjectRequest.Builder builder) ->
                builder.bucket(bucketName)
                        .key(imageKey)
                        .build()
        );
    }

    private String generateImageFileName(MultipartFile image) {
        String extension = getExtension(Objects.requireNonNull(image.getContentType()));
        if (extension == null) {
            throw new InvalidValueException(FailureCode.INVALID_IMAGE_TYPE);
        }
        return UUID.randomUUID() + extension;
    }

    private String getExtension(String contentType) {
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/heic" -> ".heic";
            case "image/heif" -> ".heif";
            default -> ".jpg";
        };
    }

    private void validateExtension(MultipartFile image) {
        String contentType = image.getContentType();
        if (!IMAGE_EXTENSIONS.contains(contentType)) {
            throw new InvalidValueException(FailureCode.INVALID_IMAGE_TYPE);
        }
    }

    private void validateFileSize(MultipartFile image) {
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new InvalidValueException(FailureCode.INVALID_IMAGE_SIZE);
        }
    }

    private static String extractImageKeyFromImageUrl(String url) {
        String basePath = "https://d2rjs92glrj91n.cloudfront.net";
        if (url.startsWith(basePath)) {
            return url.substring(basePath.length());
        } else {
            throw new BadRequestException(FailureCode.WRONG_IMAGE_URL);
        }
    }
}
