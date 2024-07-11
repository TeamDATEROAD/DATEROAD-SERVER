package org.dateroad.user.service;

import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.user.domain.User;
import org.dateroad.user.dto.response.UserInfoMainRes;
import org.dateroad.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserInfoMainRes getUserInfoMain(final Long userId) {
        User foundUser = findUserById(userId);
        return UserInfoMainRes.of(foundUser.getName(), foundUser.getTotalPoint(), foundUser.getImageUrl());
    }

    private User findUserById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
    }
}
