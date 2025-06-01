# Use an official OpenJDK runtime as the base image
FROM eclipse-temurin:17-jdk-ubi9-minimal

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container at /app
COPY target/*.jar app.jar

ARG SPRING_LIQUIBASE_CONTEXTS
ENV SPRING_LIQUIBASE_CONTEXTS=${SPRING_LIQUIBASE_CONTEXTS}

# Expose the port the app runs in
EXPOSE 8081

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]