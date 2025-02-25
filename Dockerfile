FROM openjdk:17-jdk
LABEL maintainer="choedaehyeon"

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]