package org.dateroad.user.service;

import org.assertj.core.api.Assertions;
import org.dateroad.code.FailureCode;
import org.dateroad.common.ValidatorUtil;
import org.dateroad.exception.ConflictException;
import org.dateroad.exception.InvalidValueException;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.dateroad.user.dto.request.UserSignUpReq;
import org.dateroad.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AuthService authService;

    @Test
    @DisplayName("유저가 회원가입을 한다.")
    void testDuplicatedNickName() {
        User user = User.create("성준", "dfsjalsadf", Platform.KAKAO, "null");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Assertions.assertThatThrownBy(
                () -> authService.checkNickname(user.getName())
        ).isInstanceOf(ConflictException.class);
    }
}