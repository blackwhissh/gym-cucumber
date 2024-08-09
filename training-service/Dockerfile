FROM amazoncorretto:17.0.7-alpine
WORKDIR /app
COPY ../target/training-service-0.0.1-SNAPSHOT.jar /app/training-service-0.0.1-SNAPSHOT.jar
EXPOSE 8082
CMD ["java", "-jar", "/app/training-service-0.0.1-SNAPSHOT.jar"]
