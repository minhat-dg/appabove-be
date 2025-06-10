# Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run
FROM openjdk:17-jdk-slim
COPY --from=build /app/target/springboot-app-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app.jar"]