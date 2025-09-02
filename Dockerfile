# Stage 1: Build with Maven Wrapper
FROM eclipse-temurin:21 AS build

WORKDIR /app

# Copy the Maven wrapper and config first
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (helps with caching)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the Spring Boot app
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the app with lightweight JDK
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
