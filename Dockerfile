FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle .

RUN chmod +x gradlew

RUN ./gradlew dependencies

# 모든 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew bootJar

FROM eclipse-temurin:17-jre-jammy

ENV PORT 8080

ARG JAR_FILE=build/libs/*.jar
COPY --from=builder /app/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]