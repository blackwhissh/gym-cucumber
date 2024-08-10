FROM amazoncorretto:17.0.7-alpine
WORKDIR /app
EXPOSE 8085
COPY ../target/gateway-0.0.1-SNAPSHOT.jar /app/gateway-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/app/gateway-0.0.1-SNAPSHOT.jar"] 
