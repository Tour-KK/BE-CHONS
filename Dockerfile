# java:17 이미지를 기반으로, 8081 포트로 매핑하며, build/libs에 위치한 jar파일을 실행
FROM openjdk:17
EXPOSE 8081
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]