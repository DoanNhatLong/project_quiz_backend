# Bước 1: Giữ nguyên phần Build (nếu nó đã chạy qua được bước này)
FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew build -x test

# Bước 2: THAY ĐỔI Ở ĐÂY - Dùng Amazon Corretto thay cho OpenJDK
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
EXPOSE 8080
# Copy file jar từ bước build sang
COPY --from=build /home/gradle/src/build/libs/ss6_quiz-0.0.1-SNAPSHOT.jar app.jar
# Lệnh chạy chính thức
ENTRYPOINT ["java", "-jar", "app.jar"]