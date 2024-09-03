FROM amd64/amazoncorretto:21
COPY dateroad-api/build/libs/dateroad-api-0.0.1-SNAPSHOT.jar dateroad.jar
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=dev", "-jar", "dateroad.jar"]
