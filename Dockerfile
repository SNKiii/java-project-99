
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY app/gradlew .
COPY app/gradle gradle
COPY app/build.gradle.kts .
COPY app/settings.gradle.kts .

COPY app/src src

RUN chmod +x gradlew
RUN ./gradlew bootJar -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]