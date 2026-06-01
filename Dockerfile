FROM openjdk:21-jdk-slim

WORKDIR /app

COPY app/build/libs/*.jar app.jar

RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]