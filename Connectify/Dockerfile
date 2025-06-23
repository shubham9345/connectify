# ---- STAGE 1: Build ----
# Use an official Maven image with JDK 17 to build the application
FROM maven:3.8.7-openjdk-17-slim AS build

# Set working directory
WORKDIR /connectify

# Copy Maven configuration first for dependency resolution caching
COPY /Connectify/pom.xml .
# Download dependencies (this leverages Docker cache: if pom.xml unchanged, skips re-download)
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY ./Connectify .

# Build the Spring Boot fat JAR (skip tests if desired; remove -DskipTests to run tests)
RUN mvn clean package -DskipTests -B

# ---- STAGE 2: Runtime ----
# Use a minimal JRE image. Debian-slim-based Eclipse Temurin JRE is a good choice.
FROM openjdk:17-jdk-slim

WORKDIR /Connectify

# Copy the built jar from the build stage
COPY --from=build /Connectify/target/*.jar /Connectify/app.jar

# (Optional) Expose port; adjust if your app uses a different port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
