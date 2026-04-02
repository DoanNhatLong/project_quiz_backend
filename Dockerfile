# Bước 1: Build file jar bằng Gradle
FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Chạy lệnh build để tạo file .jar
RUN ./gradlew build -x test

# Bước 2: Chạy ứng dụng bằng JDK 17
FROM openjdk:17-jdk-slim
WORKDIR /app
EXPOSE 8080
# Copy file jar từ bước build sang
COPY --from=build /home/gradle/src/build/libs/ss6_quiz-0.0.1-SNAPSHOT.jar app.jar
# Lệnh chạy chính thức
ENTRYPOINT ["java", "-jar", "app.jar"]