FROM amazoncorretto:17.0.7-alpine
WORKDIR /opt/eureka
EXPOSE 8761
COPY ../target/eureka-server-0.0.1-SNAPSHOT.jar /opt/eureka/eureka-server-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/opt/eureka/eureka-server-0.0.1-SNAPSHOT.jar"]
