# Stage 1: custom JRE 생성 (jlink 활용)
FROM amazoncorretto:21-alpine-jdk AS builder-jre
RUN apk add --no-cache binutils
RUN $JAVA_HOME/bin/jlink \
    --module-path "$JAVA_HOME/jmods" \
    --add-modules ALL-MODULE-PATH \
    --strip-debug \
    --no-man-pages \
    --compress=2 \
    --output /custom-jre

# Stage 2: 애플리케이션 빌드
FROM amazoncorretto:21-alpine-jdk AS builder
WORKDIR /app

# 전체 프로젝트 파일 복사
COPY . .

# 애플리케이션 빌드
RUN chmod +x ./gradlew
RUN ./gradlew :dateroad-api:bootJar

# Stage 3: 최종 이미지
FROM alpine:3.18.4
ENV JAVA_HOME=/custom-jre
ENV PATH="$JAVA_HOME/bin:$PATH"
ENV TZ=Asia/Seoul


WORKDIR /app

# 커스텀 JRE와 빌드된 애플리케이션 복사
COPY --from=builder-jre /custom-jre $JAVA_HOME
COPY --from=builder /app/dateroad-api/build/libs/*.jar app.jar

# 타임존 설정
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone && \
    apk del tzdata

# 애플리케이션 실행
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=dev", "app.jar"]
