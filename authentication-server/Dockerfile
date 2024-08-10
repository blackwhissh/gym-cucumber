FROM amazoncorretto:17.0.7-alpine
WORKDIR /app
COPY ../target/authentication-server-0.0.1-SNAPSHOT.jar /app/authentication-server-0.0.1-SNAPSHOT.jar
EXPOSE 8084
CMD ["java", "-jar", "/app/authentication-server-0.0.1-SNAPSHOT.jar"]
