FROM gradle:8.11-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
ENV APP_HOME=/app
ENV SPRING_PROFILES_ACTIVE=docker
WORKDIR $APP_HOME
COPY --from=build $APP_HOME/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]

