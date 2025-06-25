# 1. Build Stage
FROM gradle:8.5.0-jdk17 AS build
WORKDIR /home/gradle/project

# Gradle wrapper & dependencies만 복사
COPY --chown=gradle:gradle build.gradle settings.gradle gradlew /home/gradle/project/
COPY --chown=gradle:gradle gradle /home/gradle/project/gradle

# 종속성 캐시
RUN ./gradlew dependencies --no-daemon || true

# 나머지 소스 코드 복사
COPY --chown=gradle:gradle src /home/gradle/project/src

# 실제 빌드
RUN ./gradlew build -x test --no-daemon

# 2. Run Stage
FROM eclipse-temurin:17
VOLUME /app
COPY --from=build /home/gradle/project/build/libs/curelingo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
