# Config Server Dockerfile

# build
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src

RUN gradle build -x test --parallel --continue

# jar
FROM openjdk:17-jdk

# time zone
ENV TZ = Asia/Seoul

# jar file
RUN CP --from=builder ./build/libs/*.jar app.jar
RUN ls -al /app

# port 
EXPOSE 17652
ENTRYPOINT ["java", "-jar", "app.jar"]
