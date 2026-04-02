# Bước 1: Build file jar bằng Gradle
FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# THÊM DÒNG NÀY ĐỂ CẤP QUYỀN THỰC THI
RUN chmod +x gradlew

# Sau đó mới chạy lệnh build
RUN ./gradlew build -x test

# Bước 2: Chạy ứng dụng bằng JDK 17
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/ss6_quiz-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]