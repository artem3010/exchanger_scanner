FROM openjdk:17-oracle AS builder
LABEL authors="artem"
WORKDIR /app

CMD ["./gradlew", "clean", "bootJar"]

COPY build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]