# Config Server Dockerfile

# build
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src

RUN gradle build -x test --parallel --continue
