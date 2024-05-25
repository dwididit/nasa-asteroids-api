# Use OpenJDK 21 runtime
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the host to the container
COPY target/nasa-asteroids-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the application runs on
EXPOSE 8080

# Run the application in the foreground
CMD ["java", "-jar", "app.jar"]
