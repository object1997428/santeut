<<<<<<<< HEAD:auth/Dockerfile
# Auth Server Dockerfile
========
# Common Server Dockerfile
>>>>>>>> common:common/Dockerfile

# build
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src

RUN gradle build -x test --parallel --continue

# jar
FROM openjdk:17-jdk

WORKDIR /app
# time zone
ENV TZ=Asia/Seoul

# jar file
COPY --from=builder /app/build/libs/*.jar ./app.jar
RUN ls -al /app

# port 
<<<<<<<< HEAD:auth/Dockerfile
EXPOSE 52711
========
EXPOSE 52717
>>>>>>>> common:common/Dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
