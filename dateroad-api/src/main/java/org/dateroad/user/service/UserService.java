package org.dateroad.user.service;

import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.tag.domain.UserTag;
import org.dateroad.tag.repository.UserTagRepository;
import org.dateroad.user.domain.User;
import org.dateroad.user.dto.response.UserInfoGetMyPageRes;
import org.dateroad.user.dto.response.UserSignInRes;
import org.dateroad.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTagRepository userTagRepository;

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

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
    }
}
