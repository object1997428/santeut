## 1. 개발환경

|Tech|Stack|Version|
|:---:|:---:|:---:|
|웹서버|Nginx|1.18.0(Ubuntu)|
|WAS|Tomcat||
|**FrontEnd**|Node.js|20.11.0|
||React|18.2.0|
|**BackEnd**|OpenJDK|17|
||SpringBoot|3.2.2|
||Gradle|8.5|


## 2. 설정파일 및 환경 변수

## 포트번호 

|서버|포트번호|
|:---:|:---:|
|API gateway|17650|
|Discovery|17651|
|config|17652|

|서비스|포트번호|
|:---:|:---:|
|auth|52711|
|mountain|52712|
|guild|52713|
|party|52714|
|hiking|52715|
|community|52716|


|DB|포트번호|
|:---:|:---:|
|MySQL|33690|
|MongoDB|33691|
|Redis|33692|


## 3. 배포 시 특이사항 기재

**default.conf**
```bash
server {
        listen 80;
        listen [::]:80;
        server_name k10e201.p.ssafy.io;

        location / {
            return 301 https://$host$request_uri;
        }
}

server {

        # SSL configuration
        server_name k10e201.p.ssafy.io;
        listen 443 ssl;

        ssl_certificate "/etc/letsencrypt/live/k10e201.p.ssafy.io/fullchain.pem";
        ssl_certificate_key "/etc/letsencrypt/live/k10e201.p.ssafy.io/privkey.pem";

        location /api {
                proxy_pass http://localhost:17650;
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-Forwarded-Proto $scheme;
                proxy_http_version 1.1;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection "upgrade";

        }
}

```

### C. Docker

✅ EC2에 Docker 설치

✅ Docker Hub Login

### D. MySQL Container

✅ Docker Hub에서 MySQL 이미지 pull  
```$ docker pull mysql:8.0.35```

✅ MySQL Container 실행  
```$ docker run -d -p {외부포트}:{내부포트} --name mysql --network {네트워크} mysql:8.0.35```  

같은 네트워크 내에서 동작할 수 있도록 --network를 통해 네트워크 지정


### E. Jenkins Container

✅ Docker Hub에서 Jenkins 이미지 pull  
```$ docker pull jenkins/jenkins:latest```

✅ Jenkins Container 실행  
```$ docker run -d -p {외부포트}:{내부포트} -v /jenkins:/var/jenkins_home -v /usr/bin/docker:/usr/bin/docker -v /var/run/docker.sock:/var/run/docker.sock jenkin/jenkins:latest```

젠킨스 컨테이너가 도커를 사용할 수 있도록 볼륨 설정

## dockerfile

```Docker
# Guild Server Dockerfile

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
EXPOSE 52713
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## docker-compose.yml

```Docker
version: '3'
services:

  # MySQL, MongoDB, Redis
  mysql:
    container_name: mysql
    image: mysql:latest
    ports:
      - "33690:3306"
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: 'Ekrvnf11'
      TZ: 'Asia/Seoul'
    networks:
      - sdnetwork

  mongo:
    container_name: mongo
    image: mongo:latest
    ports:
      - "33691:27017"
    restart: always
    environment:
      MONGO_INITDB_ROOT_USERNAME: e201
      MONGO_INITDB_ROOT_PASSWORD: Ekrvnf11
    networks:
      - sdnetwork

  redis:
    container_name: redis
    image: redis:latest
    ports:
            - "33692:6379"
    restart: always
    networks:
      - sdnetwork

  # gateway, discovery, config
  gateway:
    container_name: gateway
    image: gateway-server
    ports:
      - "17650:17650"
    networks:
      - sdnetwork

  discovery:
    container_name: discovery
    image: discovery-server
    ports:
      - "17651:17651"
    networks:
      - sdnetwork

  config:
    container_name: config
    image: config-server
    ports:
      - "17652:17652"
    networks:
      - sdnetwork

  # Auth Service Server
  auth:
    container_name: auth
    image: auth-service
    ports:
      - "52711:52711"
    depends_on:
      - mysql
      - redis
    environment:
      MYSQL_HOST: 'mysql'
      MYSQL_PORT: '33690'
      MYSQL_DATABASE: 'auth'
      MYSQL_USER: 'auth'
      MYSQL_PASSWORD: 'djTm'
    networks:
      - sdnetwork

  # Mountain Service Server
  mountain:
    container_name: mountain
    image: mountain-service
    ports:
      - "52712:52712"
    depends_on:
      - mysql
    environment:
      MYSQL_HOST: 'mysql'
      MYSQL_PORT: '33690'
      MYSQL_DATABASE: mountain
      MYSQL_USER: 'mountain'
      MYSQL_PASSWORD: 'akdnsxls'
    networks:
      - sdnetwork

  # Guild Service Server
  guild:
    container_name: guild
    image: guild-service
    ports:
      - "52713:52713"
    depends_on:
      - mysql
      - redis
      - mongo
    environment:
      MYSQL_HOST: 'mysql'
      MYSQL_PORT: '33690'
      MYSQL_DATABASE: guild
      MYSQL_USER: 'guild'
      MYSQL_PASSWORD: 'rlfem'
    networks:
      - sdnetwork

  # Party Service Server
  party:
    container_name: party
    image: party-service
    ports:
      - "52714:52714"
    depends_on:
      - mysql
      - mongo
    environment:
      MYSQL_HOST: 'mysql'
      MYSQL_PORT: '33690'
      MYSQL_DATABASE: party
      MYSQL_USER: 'party'
      MYSQL_PASSWORD: 'vkxl'
    networks:
      - sdnetwork

  # Hiking Service Server
  hiking:
    container_name: hiking
    image: hiking-service
    ports:
      - "52715:52715"
    depends_on:
      - redis
    environment:
      MYSQL_HOST: 'mysql'
      MYSQL_PORT: '33690'
      MYSQL_DATABASE: party
      MYSQL_USER: 'party'
      MYSQL_PASSWORD: 'vkxl'
    networks:
      - sdnetwork

  # Community Service Server
  community:
    container_name: community
    image: community-service
    ports:
      - "52716:52716"
    depends_on:
      - mysql
    environment:
      MYSQL_HOST: 'mysql'
      MYSQL_PORT: '33690'
      MYSQL_DATABASE: community
      MYSQL_USER: 'community'
      MYSQL_PASSWORD: 'zjabslxl'
    networks:
      - sdnetwork

  # Common Service Server
  common:
    container_name: common
    image: common-service
    ports:
      - "52717:52717"
    depends_on:
      - mysql
    volumes:
      - /home/ubuntu/json-file:/app/json-file
    environment:
      MYSQL_HOST: 'mysql'
      MYSQL_PORT: '33690'
      MYSQL_DATABASE: common
      MYSQL_USER: 'common'
      MYSQL_PASSWORD: 'rhdxhd'
      FIREBASE_CONFIG_PATH: /app/json-file/santeutFirebaseAccountKey.json
    networks:
      - sdnetwork

networks:
  sdnetwork:
    external: true
```



## jenkinsFile

```
pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_FILE = "/var/jenkins_home/docker-compose.yml"
    }

    stages {
        // Checkout stage
        stage('Checkout') {
            steps {
                echo 'Checking out source code'
                git branch: 'auth', credentialsId: 'santeut', url: 'https://lab.ssafy.com/s10-final/S10P31E201.git'
            }
        }

        // Build stage
        stage('Build Service') {
            steps {
                echo 'Building Service'
                dir('auth') {
                    sh 'docker build -t auth-service .'
                }
            }
        }

        // Test stage
        stage('Test') {
            steps {
                echo 'Testing Service'
                // 여기에 테스트 관련 작업을 추가할 수 있습니다.
            }
        }

        // Deploy stage
        stage('Deploy Service') {
            steps {
                echo 'Deploying Service'
                // 이미지 빌드 후, 컨테이너 중지 및 제거
                sh 'docker-compose -f ${DOCKER_COMPOSE_FILE} stop auth || true'
                sh 'docker-compose -f ${DOCKER_COMPOSE_FILE} rm -f auth || true'
                // 새로운 컨테이너 실행
                sh 'docker-compose -f ${DOCKER_COMPOSE_FILE} up -d --force-recreate --no-deps auth'
            }
        }
    }
}
``` 