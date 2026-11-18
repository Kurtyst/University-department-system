
# Stage 1: Build Spring Boot application
FROM gradle:8.14-jdk17 AS build

WORKDIR /app

COPY . .

RUN gradle bootJar --no-daemon

# Stage 2: Run Spring Boot application
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
