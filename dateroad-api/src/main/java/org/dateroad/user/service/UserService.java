package org.dateroad.user.service;

import static org.dateroad.common.ValidatorUtil.validateTagSize;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

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
                                @Nullable final MultipartFile newImage,
                                final boolean isDefaultImage) {
        User foundUser = findUserById(userId);
        //이름 변경
        foundUser.setName(name);

        //tag 변경
        userTagRepository.deleteAllByUserId(foundUser.getId());
        validateTagSize(tags,FailureCode.WRONG_USER_TAG_SIZE);
        saveUserTag(foundUser, tags);

        //이미지 변경
        boolean isNewImageEmpty = newImage.isEmpty() || newImage == null;
        String userImage = foundUser.getImageUrl();

        // 기본이미지로 변경
        if(isNewImageEmpty && isDefaultImage) {
            //원래 이미지가 기본 이미지가 아닐 경우
            if(userImage != null) {
                deleteImage(userImage);
            }
            foundUser.setImageUrl(null);

        // 원래 이미지를 그대로 사용하거나, 새로운 이미지로 변경
        } else if(!isDefaultImage) {

            // 아요 : 이게 원래 사진 그대로 사용했거나, 새로운 사진으로 변경
            // 안드 : 원래 이미지에서 새로운 이미지
            if(userImage != null && !isNewImageEmpty) {
                deleteImage(userImage);
            }
            String newImageUrl = getImageUrl(newImage);
            foundUser.setImageUrl(newImageUrl);
        } else {
            throw new BadRequestException(FailureCode.INVALID_IMAGE_EDIT);
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

    //이미지URL 생성
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
