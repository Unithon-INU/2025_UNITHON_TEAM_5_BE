# 1. Build Stage
FROM gradle:8.5.0-jdk17 AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . /home/gradle/project
RUN gradle build -x test --no-daemon

# 2. Run Stage
FROM eclipse-temurin:17
VOLUME /app
COPY --from=build /home/gradle/project/build/libs/curelingo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]