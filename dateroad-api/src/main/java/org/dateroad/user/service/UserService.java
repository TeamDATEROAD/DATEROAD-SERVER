package org.dateroad.user.service;

import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.BadRequestException;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.s3.S3Service;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.tag.domain.UserTag;
import org.dateroad.tag.repository.UserTagRepository;
import org.dateroad.user.domain.User;
import org.dateroad.user.dto.response.UserInfoGetMyPageRes;
import org.dateroad.user.dto.response.UserInfoMainRes;
import org.dateroad.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final AuthService authService;
    @Value("${aws-property.s3-bucket-name}")
    private String path;

    @Value("${cloudfront.domain}")
    private String cachePath;
    private final UserRepository userRepository;
    private final UserTagRepository userTagRepository;
    private final S3Service s3Service;

    public UserInfoGetMyPageRes getUserInfoMyPage(final Long userId) {
        User foundUser = findUserById(userId);
        List<UserTag> userTags= userTagRepository.findAllByUserId(userId);
        List<DateTagType> dateTagTypes =
                userTags
                        .stream()
                        .map(UserTag::getDateTagType)
                        .toList();

        return UserInfoGetMyPageRes.of(foundUser.getName(), dateTagTypes, foundUser.getTotalPoint(), foundUser.getImageUrl());
    }

    @Transactional
    public void editUserProfile(final Long userId,
                                final String name,
                                final List<DateTagType> tags,
                                final MultipartFile newImage) throws IOException, ExecutionException, InterruptedException {
        User foundUser = findUserById(userId);

        //이름 변경
        foundUser.setName(name);

        //tag 변경
        userTagRepository.deleteAllByUserId(foundUser.getId());
        authService.validateUserTagSize(tags);
        saveUserTag(foundUser, tags);

        //이미지 변경
        String userImage = foundUser.getImageUrl();

        // 1. 원래 이미지가 있다가 null로 변경
        if (userImage != null && newImage == null) {
            deleteImage(userImage);
            foundUser.setImageUrl(null);

        // 2. 원래 이미지가 있다가 새로운 이미지로 변경
        }  else if (userImage != null && newImage != null) {
            deleteImage(userImage);
            String newImageUrl = uploadImage(newImage);
            foundUser.setImageUrl(newImageUrl);

        // 3. 원래 이미지가 없다가 새로운 이미지로 변경
        } else if (userImage == null && newImage != null) {
            String newImageUrl = uploadImage(newImage);
            foundUser.setImageUrl(newImageUrl);
        }
        userRepository.save(foundUser);
    }

    public UserInfoMainRes getUserInfoMain(final Long userId) {
        User foundUser = findUserById(userId);
        return UserInfoMainRes.of(foundUser.getName(), foundUser.getTotalPoint(), foundUser.getImageUrl());
    }

    private User findUserById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
    }

    //유저 태그 수정
    public void saveUserTag(final User savedUser, final List<DateTagType> userTags) {
        List<UserTag> list =  userTags.stream()
                .map(dateTagType -> UserTag.create(savedUser, dateTagType))
                .toList();
        userTagRepository.saveAll(list);
    }

    private void deleteImage(final String imageUrl) {
        try {
            s3Service.deleteImage(imageUrl);
        } catch (IOException e) {
            throw new BadRequestException(FailureCode.WRONG_IMAGE_URL);
        }
    }

    private String uploadImage(final MultipartFile newImage) {
        try {
            return cachePath + s3Service.uploadImage(path, newImage).get();
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new BadRequestException(FailureCode.WRONG_IMAGE_URL);
        }
    }
}
