FROM amazoncorretto:17.0.7-alpine
WORKDIR /app
COPY ../target/gym-service-3.2.5.jar /app/gym-service-3.2.5.jar
EXPOSE 8081
CMD ["java", "-jar", "/app/gym-service-3.2.5.jar"]
