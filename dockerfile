FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /api_auth

COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle/

COPY src src/

RUN ./gradlew bootJar

FROM eclipse-temurin:25-jre-alpine

WORKDIR /api_auth

COPY --from=builder /api_auth/build/libs/auth-api-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar" ,"auth-api-0.0.1-SNAPSHOT.jar"]