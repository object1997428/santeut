# Auth Server Dockerfile

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

<<<<<<< HEAD:auth/Dockerfile
# port 
EXPOSE 52711
=======
# expose port
EXPOSE 17650

# set entry point
>>>>>>> 9af070922a771ed963693af74c97af19a467f620:gateway/Dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
