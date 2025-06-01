# Use an official OpenJDK runtime as the base image
FROM eclipse-temurin:17-jdk-ubi9-minimal

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container at /app
COPY target/*.jar app.jar

# Accept build-time arguments for Liquibase context and changelog
ARG SPRING_LIQUIBASE_CONTEXTS
ARG SPRING_LIQUIBASE_CHANGELOG

# Set them as environment variables so Spring Boot can access them
ENV SPRING_LIQUIBASE_CONTEXTS=${SPRING_LIQUIBASE_CONTEXTS}
ENV SPRING_LIQUIBASE_CHANGELOG=${SPRING_LIQUIBASE_CHANGELOG}

# Expose the port the app runs in
EXPOSE 8081

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]