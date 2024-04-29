# Gateway Server Dockerfile

# build
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src

RUN gradle build -x test --parallel --continue

# jar
FROM openjdk:17-jdk

# set time zone
ENV TZ=Asia/Seoul

# copy JAR file
COPY --from=builder /app/build/libs/app.jar /app/
RUN ls -al /app

# expose port
EXPOSE 17650

# set entry point
ENTRYPOINT ["java", "-jar", "app.jar"]
