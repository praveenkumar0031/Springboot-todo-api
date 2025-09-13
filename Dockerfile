# syntax=docker/dockerfile:1

# Stage 1: Build with Maven Wrapper
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy wrapper and pom first for better caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make wrapper executable and warm up the cache
RUN chmod +x mvnw && ./mvnw -B -q -DskipTests dependency:go-offline

# Copy sources and build
COPY src ./src
RUN ./mvnw -B  clean package -DskipTests

# Stage 2: Run the app with lightweight JRE
FROM eclipse-temurin:21-jre
WORKDIR /app

# Optional: run as non-root
RUN useradd -ms /bin/sh spring
USER spring

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

