
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/myapp.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]