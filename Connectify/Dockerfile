# ---- STAGE 1: Build ----
FROM maven:3.8.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven config and fetch dependencies
COPY Connectify/pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY Connectify/src ./src
RUN mvn clean package -DskipTests -B

# ---- STAGE 2: Runtime ----
# Use a slim Eclipse Temurin JRE image for smaller runtime
FROM eclipse-temurin:17-jre-jammy AS runtime

WORKDIR /app

# Copy the built jar
COPY --from=build /app/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
