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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    AuthService authService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("유저가 회원가입을 한다.")
    void testDuplicatedNickName() {

        //given
        User user = User.create("성준", "dfsjalsadf", Platform.KAKAO, "null");

        //when
        userRepository.save(user);

        //then
        Assertions.assertThatThrownBy(
                () -> authService.checkNickname("성준")
        ).isInstanceOf(ConflictException.class);
    }
}