FROM gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY build.gradle settings.gradle /app/
COPY src /app/src
RUN gradle build -x test --parallel --continue

FROM openjdk:17-jdk
ENV TZ=Asia/Seoul
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
EXPOSE 17652
ENTRYPOINT ["java", "-jar", "app.jar"]
