# E-commerce Application

SpringBoot web application that integrates with a PostgreSQL database and uses Docker to run in a container. The application should have a REST API that allows users to perform CRUD (create, read, update, delete) operations on a data model representing a simple e-commerce platform. The REST API should be defined using OpenAPI.

## Prerequisites
- JDK 21
- Maven 3.9.0
- Docker

## Build app

1.  Clone the repository:

```sh
git clone https://github.com/yourusername/ecommerce-application.git
cd ecommerce-application
```
2. Build the application using Maven:

```sh
mvn clean install
```
This command will compile the project, run the tests, and package the application into a JAR file.

## Running with Docker

1. Build the Docker image for the application:
```sh
docker build -t ecommerce-app .
```
2. Start the application and the PostgreSQL database using Docker Compose:
```sh
docker-compose up
```
3. The application should now be running at http://localhost:8080, and the PostgreSQL database should be accessible at jdbc:postgresql://localhost:5432/ecommerce.

## Technologies
- Java 21
- OpenApi 3.0
- Spring Boot
- Maven 
- PostgreSQL
- Hibernate
- JUnit 5
- Mockito
- Docker