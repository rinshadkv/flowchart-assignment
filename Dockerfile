
FROM openjdk:21-slim

WORKDIR /app

COPY target/flowchart-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
