FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -Dmaven.test.skip=true

FROM eclipse-temurin:25-jre-alpine AS runtime

RUN apk add --no-cache wget \
    && addgroup -S spring \
    && adduser -S spring -G spring

WORKDIR /app

COPY --from=build /app/target/push-notification-service-*.jar app.jar
RUN chown spring:spring app.jar

USER spring:spring

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dfile.encoding=UTF-8" \
    SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD wget -qO- "http://127.0.0.1:${SERVER_PORT}/actuator/health" > /dev/null || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
