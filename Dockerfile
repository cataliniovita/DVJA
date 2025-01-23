# Use an official Java runtime as a parent image
FROM openjdk:17

# Set the working directory
WORKDIR /app

# Copy the project jar file to the container
COPY ./target/bplanner-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that your application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
