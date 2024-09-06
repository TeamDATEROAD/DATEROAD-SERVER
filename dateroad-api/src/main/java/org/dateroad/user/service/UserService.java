package org.dateroad.user.service;

import static org.dateroad.common.ValidatorUtil.validateTagSize;

import io.micrometer.common.lang.Nullable;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.BadRequestException;
import org.dateroad.exception.ConflictException;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.image.service.ImageService;
import org.dateroad.s3.S3Service;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.tag.domain.UserTag;
import org.dateroad.tag.repository.UserTagRepository;
import org.dateroad.user.domain.User;
import org.dateroad.user.dto.response.UserInfoGetMyPageRes;
import org.dateroad.user.dto.response.UserInfoMainRes;
import org.dateroad.user.repository.UserRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserTagRepository userTagRepository;
    private final S3Service s3Service;
    private final ImageService imageService;

    public UserInfoGetMyPageRes getUserInfoMyPage(final Long userId) {
        User foundUser = userRepository.findUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
        List<UserTag> userTags = userTagRepository.findAllByUserId(userId);
        List<DateTagType> dateTagTypes =
                userTags
                        .stream()
                        .map(UserTag::getDateTagType)
                        .toList();
        return UserInfoGetMyPageRes.of(foundUser.getName(), dateTagTypes, foundUser.getTotalPoint(),
                foundUser.getImageUrl());
    }

    @Transactional
    @CachePut(cacheNames = "user", key = "#userId", unless = "#result == null", cacheManager = "cacheManagerForOne")
    public User editUserProfile(final Long userId,
                                final String name,
                                final List<DateTagType> tags,
                                @Nullable final MultipartFile newImage,
                                final boolean isDefaultImage) {
        User foundUser = userRepository.findUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
        checkDuplicateNickname(name, foundUser);
        foundUser.setName(name);
        userTagRepository.deleteAllByUserId(foundUser.getId());
        validateTagSize(tags, FailureCode.WRONG_USER_TAG_SIZE);
        saveUserTag(foundUser, tags);
        boolean isNewImageNull = newImage == null;
        String userImage = foundUser.getImageUrl();
        if (isNewImageNull && isDefaultImage) {
            if (userImage != null) {
                deleteImage(userImage);
            }
            foundUser.setImageUrl(null);
        } else if (!isDefaultImage) {
            if (userImage != null && !isNewImageNull) {
                deleteImage(userImage);
                String newImageUrl = imageService.getImageUrl(newImage);
                foundUser.setImageUrl(newImageUrl);
            }
            else if (userImage == null && !isNewImageNull) {
                String newImageUrl = imageService.getImageUrl(newImage);
                foundUser.setImageUrl(newImageUrl);
            }
        } else {
            throw new BadRequestException(FailureCode.INVALID_IMAGE_EDIT);
        }
        return userRepository.save(foundUser);
    }

    public UserInfoMainRes getUserInfoMain(final Long userId) {
        User foundUser = userRepository.findUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
        return UserInfoMainRes.of(foundUser.getName(), foundUser.getTotalPoint(), foundUser.getImageUrl());
    }

    public void saveUserTag(final User savedUser, final List<DateTagType> userTags) {
        List<UserTag> list = userTags.stream()
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

    private void checkDuplicateNickname(final String nickname, final User foundUser) {
        if (userRepository.existsByName(nickname)) {
            if (!foundUser.getName().equals(nickname)) {
                throw new ConflictException(FailureCode.DUPLICATE_NICKNAME);
            }
        }
    }
}
