FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/ecommerce-0.0.1-SNAPSHOT.jar /app/ecommerce-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/ecommerce-0.0.1-SNAPSHOT.jar"]
