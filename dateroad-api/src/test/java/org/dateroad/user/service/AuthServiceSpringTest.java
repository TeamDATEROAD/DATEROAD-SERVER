package org.dateroad.user.service;

import lombok.extern.slf4j.Slf4j;
import org.dateroad.exception.ConflictException;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.tag.domain.UserTag;
import org.dateroad.tag.repository.UserTagRepository;
import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.dateroad.user.dto.request.UserSignUpReq;
import org.dateroad.user.facade.AuthFacade;
import org.dateroad.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Slf4j
@SpringBootTest
class AuthServiceSpringTest {
    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTagRepository userTagRepository;

    @Value("${test.oauth.token1}")
    private String testToken1;

    @Value("${test.oauth.token2}")
    private String testToken2;

    private static final UserSignUpReq USER1_SIGN_UP_REQ = new UserSignUpReq("테스트유저1", Platform.KAKAO);
    private static final UserSignUpReq USER2_SIGN_UP_REQ = new UserSignUpReq("테스트유저2", Platform.KAKAO);
    private static final List<DateTagType> USER1_TAGS = List.of(DateTagType.DRIVE, DateTagType.SHOPPING);
    private static final List<DateTagType> USER2_TAGS = List.of(DateTagType.DRIVE, DateTagType.HEALING);

    @AfterEach
    void cleanup() {
        userTagRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("동일한 회원가입 요청이 동시에 들어올 경우 중복 회원가입이 방지된다.")
    @Test
    void preventDuplicateSignUp() throws InterruptedException {
        // given
        int threadCount = 10; // 동시 요청 개수
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    authFacade.lettuceSignUp(testToken1, USER1_SIGN_UP_REQ, null, USER1_TAGS);
                } catch (ConflictException e) {
                    log.error("회원가입 동시성 테스트 중 예외 발생: ", e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 요청이 완료될 때까지 대기
        executorService.shutdown();

        // then
        assertThat(userRepository.count()).isEqualTo(1); // 중복 회원가입이 발생하지 않아야 함
    }

    @DisplayName("서로 다른 토큰으로(사용자가) 동시에 회원가입 요청 시 모두 회원가입이 성공한다.")
    @Test
    void signUpWithDifferentTokensLatch() throws InterruptedException {
        // given
        int threadCount = 2; // 두 명의 동시 요청
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1); // 요청 시작 신호
        CountDownLatch latch = new CountDownLatch(threadCount); // 요청 완료 대기

        // when
        executorService.submit(() -> {
            try {
                startLatch.await(); // 모든 스레드가 시작 신호를 기다림
                authFacade.lettuceSignUp(testToken1, USER1_SIGN_UP_REQ, null, USER1_TAGS);
            } catch (Exception e) {
                log.error("회원가입 동시성 테스트 중 예외 발생 (user1): ", e);
            } finally {
                latch.countDown();
            }
        });

        executorService.submit(() -> {
            try {
                startLatch.await(); // 모든 스레드가 시작 신호를 기다림
                authFacade.lettuceSignUp(testToken2, USER2_SIGN_UP_REQ, null, USER2_TAGS);
            } catch (Exception e) {
                log.error("회원가입 동시성 테스트 중 예외 발생 (user2): ", e);
            } finally {
                latch.countDown();
            }
        });

        // 모든 요청이 동시에 시작되도록 신호를 줌
        startLatch.countDown();

        latch.await(); // 모든 요청이 완료될 때까지 대기
        executorService.shutdown();

        // then
        assertThat(userRepository.count()).isEqualTo(2); // 두 명 모두 가입되어야 함
    }

    @DisplayName("서로 다른 토큰으로(사용자가) 동시에 회원가입 요청 시 모두 회원가입이 성공한다.")
    @Test
    void signUpWithDifferentTokensCallable() throws InterruptedException, ExecutionException {
        // given
        int threadCount = 2; // 동시 요청 개수
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount); // 가용 쓰레드 제한
        List<Callable<String>> tasks = new ArrayList<>();

        // Callable tasks 생성
        tasks.add(() -> {
            authFacade.lettuceSignUp(testToken1, USER1_SIGN_UP_REQ, null, USER1_TAGS);
            return "USER1 회원가입 성공";
        });

        tasks.add(() -> {
            authFacade.lettuceSignUp(testToken2, USER2_SIGN_UP_REQ, null, USER2_TAGS);
            return "USER2 회원가입 성공";
        });

        System.out.println("-------작업 실행------");
        List<Future<String>> results = executorService.invokeAll(tasks); // 모든 요청 실행
        System.out.println("-------작업 종료------");

        // then
        System.out.println("-------결과 출력------");
        for (Future<String> result : results) {
            System.out.println(result.get()); // 작업 결과 출력
        }

        executorService.shutdown();

        // 추가 검증
        assertThat(userRepository.count()).isEqualTo(2); // 두 명 모두 가입되어야 함
    }

    @DisplayName("단일 회원가입 요청 시 User와 UserTag가 정상적으로 저장된다.")
    @Test
    void signUpUserAndTagsSaved() {

        // when
        authFacade.lettuceSignUp(testToken1, USER1_SIGN_UP_REQ, null, USER1_TAGS);

        // then
        // 저장된 User 확인
        long userCount = userRepository.count();
        assertThat(userCount).isEqualTo(1);

        User savedUser = userRepository.findAll().getFirst(); // 유일한 User 가져오기
        assertThat(savedUser.getName()).isEqualTo("테스트유저1");
        assertThat(savedUser.getPlatForm()).isEqualTo(Platform.KAKAO);

        // 저장된 UserTag 확인
        List<UserTag> savedTags = userTagRepository.findAllByUserId(savedUser.getId());

        // 개별 태그 확인
        assertThat(savedTags.stream().anyMatch(tag -> tag.getDateTagType() == DateTagType.DRIVE)).isTrue();
        assertThat(savedTags.stream().anyMatch(tag -> tag.getDateTagType() == DateTagType.SHOPPING)).isTrue();

        // 로그 출력
        log.info("저장된 User: {}", savedUser);
        log.info("저장된 UserTags: {}", savedTags);
    }
}