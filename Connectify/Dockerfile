# ---- STAGE 1: Build ----
# Use an official Maven image with JDK 17 to build the application
FROM maven:3.8.7-openjdk-17-slim AS build

# Set working directory
WORKDIR /workspace

# Copy Maven configuration first for dependency resolution caching
COPY pom.xml .
# If you have a settings.xml or parent POM, copy them as needed:
# COPY settings.xml .
# COPY parent-pom.xml .

# Download dependencies (this leverages Docker cache: if pom.xml unchanged, skips re-download)
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Build the Spring Boot fat JAR (skip tests if desired; remove -DskipTests to run tests)
RUN mvn clean package -DskipTests -B

# ---- STAGE 2: Runtime ----
# Use a minimal JRE image. Debian-slim-based Eclipse Temurin JRE is a good choice.
FROM eclipse-temurin:17-jre-jammy

# Set a non-root user if desired (uncomment to enhance security)
# RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
# USER appuser

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /workspace/target/*.jar app.jar

# (Optional) Expose port; adjust if your app uses a different port
EXPOSE 8080

# Use a simple entrypoint. You can pass JVM options via environment variable if desired.
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
