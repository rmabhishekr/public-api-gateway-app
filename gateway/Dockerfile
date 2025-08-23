# Use Maven with JDK 17 to build the app
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy project files
COPY . .

# Package the application (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Use a lightweight JDK to run the app
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the jar file from the build stage
COPY target/gateway-0.0.1-SNAPSHOT.jar gateway.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","gateway.jar"]