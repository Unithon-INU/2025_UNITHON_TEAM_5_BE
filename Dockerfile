# 1. Build Stage
FROM gradle:8.5.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project

RUN gradle build -x test --no-daemon

# 2. Run Stage
FROM eclipse-temurin:17
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar

COPY --from=build /home/gradle/project/${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]