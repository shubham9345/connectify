# ---- STAGE 1: Build ----
FROM maven:3.8.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven config and fetch dependencies
# We use ./ because pom.xml is in the same directory as the Dockerfile
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---- STAGE 2: Runtime ----
FROM eclipse-temurin:17-jre-jammy AS runtime

WORKDIR /app

# Copy the built jar from the build stage
# Maven builds the jar into the 'target' folder by default
COPY --from=build /app/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
