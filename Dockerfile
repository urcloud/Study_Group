# 베이스 이미지로 OpenJDK 17 사용
FROM openjdk:17.0.2-oracle

# 작업 디렉터리 설정
WORKDIR /app

# 빌드된 Jar 파일을 컨테이너로 복사
COPY build/libs/*.jar app.jar

# 포트 설정
EXPOSE 8080

# 컨테이너 실행 시 Jar 실행
ENTRYPOINT ["java","-jar","app.jar"]
