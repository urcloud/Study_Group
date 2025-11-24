# Docker-Compose.yaml 사용 안하는 경우
## 1. Docker 사용에 필요한 파일
- Dockerfile: 애플리케이션을 이미지로 만들기 위한 설정 파일

## 2. 프로젝트 루트에 Dockerfile 생성
> FROM openjdk:17-jdk-alpine
- Docker 이미지의 베이스 이미지를 지정: openjdk:17-jdk-alpine은 OpenJDK 17 JDK가 설치된 이미지
- JDK를 포함하고 있으므로, Java 애플리케이션을 빌드 및 실행 가능
> WORKDIR /app
- 컨테이너 내부에서 작업 디렉터리를 설정
- 이 명령어 이후 실행되는 모든 명령(COPY, RUN, ENTRYPOINT)은 /app 디렉터리를 기준으로 작동
> COPY build/libs/*.jar app.jar
- 호스트 머신에서 빌드된 JAR 파일을 컨테이너 내부로 복사
- build/libs/*.jar는 일반적으로 Gradle 빌드 결과물 경로
- app.jar로 이름을 바꾸어 컨테이너 내부 /app 디렉터리에 복사
> EXPOSE 8080
- 컨테이너가 열어 놓을 포트를 지정
- Spring Boot 기본 포트가 8080이므로, 외부에서 컨테이너의 8080 포트에 접근 가능하도록 설정
> ENTRYPOINT ["java","-jar","app.jar"]
- 컨테이너가 실행될 때 자동으로 실행될 명령을 지정
- "java", "-jar", "app.jar"는 Java 애플리케이션을 실행하는 표준 명령
- ENTRYPOINT를 사용하면 컨테이너 실행 시 항상 이 명령이 실행됨

## 3. Jar 파일 빌드
```bash
./gradlew clean bootJar
```
- build/libs 폴더 안에 Study_Group-0.0.1-SNAPSHOT.jar 같은 파일이 생성됨
- 이 Jar가 나중에 Docker 컨테이너 안에서 실행할 파일

## 4. Docker 이미지 빌드
```bash
docker build -t study-app .
```
- study-app: 만들어질 Docker 이미지 이름
- .: 현재 디렉터리 기준으로 Dockerfile과 Jar 복사

```bash
docker images
```
- study-app 이미지가 생성되었는지 확인

## 5. MySQL 컨테이너 준비
```bash
docker run -d \
  --name study-mysql \
  -e MYSQL_ROOT_PASSWORD= \
  -e MYSQL_DATABASE=studygroup \
  -p 3307:3306 \
  -v mysql-data:/var/lib/mysql \
  mysql:8.0
```
- -p 3307:3306: 호스트 포트 3307, 컨테이너 내부 포트 3306
- -v mysql-data:/var/lib/mysql: 데이터 영속화

```bash
docker exec -it study-mysql mysql -u root -p
```
- MYSQL 준비 확인

## 6. Spring Boot 컨테이너 실행
```bash
docker run -d \
  --name study-app \
  -p 8080:8080 \
  --link study-mysql:mysql \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/studygroup \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD= \
  study-app
```
- --link study-mysql:mysql: Spring Boot 컨테이너에서 mysql 호스트명으로 MySQL 접속 가능
- 환경변수로 DB URL, 사용자, 비밀번호 설정

## 7. 컨테이너 실행 확인
```bash
docker ps
docker logs study-app
```
- Spring Boot가 정상적으로 Started Application 로그가 뜨면 성공
- 브라우저에서 http://localhost:8080 접속 테스트 가능 (백엔드 프로젝트라 POSTMAN으로 확인 가능)

## 8. 데이터 확인
```bash
docker exec -it study-mysql mysql -u root -p
```

```bash
USE studygroup;
SHOW TABLES;
SELECT * FROM member;
```
- 내부 MYSQL 사용 가능


# Docker-compose.yaml 사용하는 경우
## 1. Docker Compose 설정
- docker-compose.yaml: 여러 컨테이너(MySQL + Spring Boot)를 한 번에 관리할 때 사용

```yaml
version: '3.9'

services:
  mysql:
    image: mysql:8.0
    container_name: study-mysql
    environment:
      MYSQL_ROOT_PASSWORD: 
      MYSQL_DATABASE: studygroup
    ports:
      - "3307:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - study-network
    healthcheck:
      test: [ "CMD-SHELL", "mysqladmin ping -uroot -p1234" ]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 10s

  app:
    build: .
    container_name: study-app
    ports:
      - "8080:8080"
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/studygroup
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: 
    networks:
      - study-network

networks:
  study-network:
    driver: bridge

volumes:
  mysql-data:
```
> version: '3.9'
- : Docker Compose 파일 형식의 버전
> services:
- 실행할 컨테이너 정의, mysql과 app 두 개의 서비스 정의
> mysql:
- MySQL 8 컨테이너
> image: mysql:8.0
- Docker Hub에 있는 공식 MySQL 8.0 이미지를 사용, Docker는 없으면 자동으로 pull함
> container_name: study-mysql
- 컨테이너의 이름을 study-mysql로 지정, 지정하지 않으면 자동으로 임의의 이름이 생성됨
> environment
- MySQL 컨테이너의 환경 변수를 설정함
- MYSQL_ROOT_PASSWORD: 루트 계정 비밀번호
- MYSQL_DATABASE: 컨테이너 시작 시 자동으로 생성할 데이터베이스 이름, 여기서는 studygroup 데이터베이스가 자동으로 만들어짐
> ports
- "3307:3306" : 호스트 포트 3307을 컨테이너 내부의 3306(MySQL 기본 포트) 과 연결, 로컬 PC에서 localhost:3307로 접속하면 컨테이너 내부의 MySQL로 연결됨
> volumes
- mysql-data:/var/lib/mysql : MySQL의 데이터를 호스트의 Docker 볼륨(mysql-data) 에 영구 저장, 컨테이너를 삭제해도 데이터는 남음
> networks
- study-network : 같은 네트워크에 속한 컨테이너끼리는 DNS 이름으로 서로 접근 가능, app 컨테이너가 mysql이라는 이름으로 DB에 접근 가능
> healthcheck:
- 주로 애플리케이션이 준비되었는지 확인하는 용도
- MySQL이 정상적으로 시작되면 mysqladmin ping 명령이 성공 → 컨테이너 healthy
- 실패하면 unhealthy 상태로 표시
- 상태 체크 주기, 체크 명령 최대 실행 시간, 실패 후 재시도 횟수, 컨테이너 시작 후 체크를 시작하기까지 대기 시간 설정

> app:
- Spring Boot 컨테이너
> build: .
- 현재 디렉터리(.)에 있는 Dockerfile을 이용해 이미지를 빌드함, Dockerfile (openjdk:17-jdk-alpine)이 여기에 사용됨
> container_name: study-app
- 컨테이너 이름을 study-app으로 지정함
> ports
- "8080:8080" : 호스트 8080 포트를 컨테이너 내부 8080 포트에 매핑, 브라우저에서 http://localhost:8080 으로 접속하면 Spring Boot 앱에 접근할 수 있음
> depends_on
- mysql : app 컨테이너는 mysql이 먼저 시작된 후 실행됨, 순서를 보장하지만, MySQL이 완전히 초기화될 때까지 기다려주지는 않음
- Spring Boot가 너무 빨리 실행되면 Connection refused 에러가 날 수 있음
> condition: service_healthy
- mysql이 healthy 상태가 될 때까지 기다림
> environment
- Spring Boot 앱의 환경 변수 설정, 내부적으로 application.properties 또는 application.yml에서 자동 매핑됨
> SPRING_DATASOURCE_URL:
- MySQL 연결 URL, mysql은 서비스 이름이므로 Docker 네트워크 내에서 DNS처럼 동작함 (즉, localhost 대신 mysql 사용)
> SPRING_DATASOURCE_USERNAME:
- DB 사용자 이름 (root)
> SPRING_DATASOURCE_PASSWORD:
- DB 비밀번호로, 위에서 MySQL MYSQL_ROOT_PASSWORD와 일치해야 함
> networks
- study-network : MySQL과 같은 네트워크에 참여시켜, 두 컨테이너 간 통신 가능하게 함

> driver: bridge
- 도커 컨테이너들이 서로 통신하는 방법 정의
- Docker의 기본 네트워크 드라이버로, 같은 호스트(PC) 안에서 여러 컨테이너가 통신할 때 가장 일반적으로 사용하는 방식
> volumes: mysql-data:
- 컨테이너를 껐다 켜도 DB 데이터가 날아가지 않게 도와주는 역할
- mysql-data라는 이름의 영구 저장소(volume)를 하나 만들어서 관리하게끔 함

## 2. Jar 파일 빌드
```bash
./gradlew clean bootJar
```

## 3. docker-compose로 빌드+실행
```bash
docker-compose up --build
```

- 로그 확인
```bash
docker compose logs -f app
docker compose logs -f mysql
```

- 컨테이너 상태 확인
```bash
docker ps
```

# docker-compose.yaml 스프링 자바 내부 동작과정
1. docker-compose CLI가 docker-compose.yaml을 읽음
2. YAML을 파싱해서 서비스 목록, 의존성, 환경변수, 네트워크 구성 등을 Docker Engine API 명령으로 변환 
-  Compose 자체가 직접 컨테이너를 실행하는 게 아니라, Docker 데몬(dockerd)에게 이런 컨테이너들을 이렇게 띄워줘 하고 요청을 보냄
- Docker 데몬은 Docker 서버
3. 우리가 설정한 Bridge 네트워크가 자동으로 만들어지고, 컨테이너 이름이 곧 DNS 호스트 이름이 됨
4. 컨테이너를 읽어서 image:가 있으면 해당 이미지를 Pull (없으면 빌드), build:가 있으면 Dockerfile을 기반으로 Image Build, 환경변수 설정 (environment:), 포트 매핑 (ports:), 볼륨 마운트 (volumes:), 네트워크 연결
5. depends_on을 참고해서 의존성이 있는 컨테이너를 먼저 실행
- ex) mysql -> app 순서로
6. depends_on은 단순히 시작 순서만 보장하지, 완전히 준비(ready) 되었는지는 모름, 따라서 실제 서비스에서는 헬스체크(healthcheck) 나 retry 로직을 추가함
7. Spring Boot가 시작되며 application.yml 또는 환경변수에서 설정을 읽음
8. 내부적으로 MySQL, Redis에 TCP 연결 시도, docker network 덕분에 IP 대신 이름으로 접근 가능
9. 각 연결이 성공하면 애플리케이션이 정상적으로 구동됨

## + Spring Boot는 docker-compose.yaml을 직접 읽지 않음
- docker-compose.yaml은 Spring Boot와는 완전히 별개로, Docker 엔진이 해석하고 실행하는 설정 파일임
- Spring Boot는 단지 컨테이너 내부의 환경변수나 네트워크 설정을 통해 간접적으로 그 설정의 결과만 이용함

# 하나의 애플리케이션인데 MYSQL 이미지를 여러 개 정의했을 때, 실행 환경(버전, 설정 등)이 동일할 때
- 이미지는 재사용되고, 컨테이너는 각각 새로 만들어짐
- 컨테이너는 각각 고유한 데이터 저장소(볼륨)과 프로세스 공간을 가짐

## 1. 하나의 이미지를 여러 서비스가 사용할 때
- 이미지는 하나, 컨테이너는 두 개
- 이미지는 새로 만들지 않고, 컨테이너는 서비스마다 하나씩 새로 생성해 실행환경은 동일하지만 데이터, 포트가 분리됨

## 2. MYSQL 이미지를 아예 여러 개로 빌드해놧을 때
- 각각으로부터 컨테이너가 새로 만들어짐
- 내부 환경이 동일하더라도 이미지가 다르면 Docker는 완전히 별개로 인식해 실행 환경이 같더라도 이름이 다른 이미지면 Docker은 서로 다른 템플릿으로 취급함

## + 왜 같은 이미지를 쓰더라도 컨테이너는 새로 만들어야 하는지
- Docker의 격리 원칙 때문임
- 컨테이너는 프로세스, 네트워크 인터페이스, 파일시스템 (쓰기 가능한 Layer), 환경변수를 독립적으로 유지해야 함
- 컨테이너는 실행 단위기 때문에 하나의 MYSQL 프로세스를 여러 앱이 같이 쓰는 개념이 없음

* HTTP는 데이터 평문 전송, HTTPS는 SSL/TLS 암호화를 통해 데이터를 암호화하고 웹사이트의 신원을 확인하여 보안과 무결성을 강화
* HTTP에서 HTTPS로 배포하려면, SSL/TLS 인증서를 발급받아 웹 서버에 설치하고, HTTP 요청을 HTTPS로 자동 리디렉션하도록 설정해야 함
* SSL 인증서 발급을 위해 개인키 및 CSR(Certificate Signing Request)를 생성하고, 웹 서버의 종류에 따라 인증서 설치 가이드를 따라야 함

+ 할 일
- 톰캣스프링부트도 전용웹서버, nginX 배포하는 데 있어 동작 원리나 과정 (로드밸런싱, 전문적인 웹 서버)
- 무중단배포 블루그린, CICD 알아오기, githubaction이랑 같이
- traefik은 뭔지 http/https
- apache 서버는? 얘가 제일 오래된 것
- yaml 비밀번호 환경변수 파일 분리
- aws 배포과정 찾아보기, 이미지를 배포하는지 compose 파일을 배포하는건지 자세히 동작과정 알아보기
- mysql을 하나로 밖으로 빼고 다른 앱 2개가 각각 mysql을 참조할 수 있게끔 하는 방법 (mysql 사용하는 간단한 app 하나 도커 사이트에 예제 찾아오기)