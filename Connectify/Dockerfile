# ---- STAGE 1: Build ----
FROM maven:3.8.5-openjdk-17 AS build

# Set a consistent working directory inside the build image
WORKDIR /app

# Copy Maven config for dependency caching
COPY Connectify/pom.xml .

# Pre-fetch dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY Connectify/src ./src
# If you have other files (e.g., resources outside src), copy them too:
# COPY Connectify/src/main/resources ./src/main/resources
# COPY Connectify/settings.xml (or other config) if needed

# Build the Spring Boot fat JAR
RUN mvn clean package -DskipTests -B

# ---- STAGE 2: Runtime ----
FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app

# Copy the built jar from the build stage; adjust wildcard if you know exact name
COPY --from=build /app/target/*.jar ./app.jar

# Expose port (adjust if your app uses a different port)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
