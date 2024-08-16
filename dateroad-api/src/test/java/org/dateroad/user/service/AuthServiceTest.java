package org.dateroad.user.service;

import org.assertj.core.api.Assertions;
import org.dateroad.exception.ConflictException;
import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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