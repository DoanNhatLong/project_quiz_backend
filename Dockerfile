# B1: Dùng JDK 17 (Cái này máy ảo nào cũng chạy được)
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# B2: Copy toàn bộ dự án vào
COPY . .

# B3: Cấp quyền thực thi cho file gradlew và chạy nó để build
# Dùng ./gradlew thay vì gradle để nó tự lấy bản chuẩn của dự án bạn
RUN chmod +x ./gradlew
RUN ./gradlew bootJar -x test

# B4: Giai đoạn chạy (như cũ)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]