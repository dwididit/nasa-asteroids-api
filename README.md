# NASA Asteroids API

This project is a Java-based RESTful application that interacts with the NASA Asteroids API. It provides endpoints to retrieve information about asteroids.

## Features

- Fetch data for top 10 asteroids closest to the earth.
- Fetch information about a specific asteroid by ID.
- Return data in a structured JSON format.
- Handle errors gracefully with custom exception handling.

## Technologies Used

- Java
- Spring Boot
- Spring Web
- Lombok
- PostgreSQL
- NASA Asteroids NeoWs (Near Earth Object Web Service)
- Docker

## Prerequisites

- Java 21 or higher
- Maven
- PostgreSQL 16 or later
- Docker
- NASA API Key

## Architecture

![Flow Diagram](http://dwidi.dev/wp-content/uploads/2024/05/Vue-Spring.png)

## Getting Started

### Clone the Repository

```bash
gh repo clone dwididit/nasa-asteroids-api
cd nasa-asteroids-api
```

### Configuration

Configuration for application.properties file in the src/main/resources directory:

```bash
# Application name
spring.application.name=nasa-asteroids-api

# NASA API configuration
nasa.api.url=https://api.nasa.gov
nasa.api.key=YOUR_NASA_API_KEY

# Port Configuration
server.port=8080

# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/nasa_asteroid
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Logging configuration
logging.level.org.springframework=INFO
logging.level.root=ERROR
logging.file.name=logs/application.log 
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} - %msg%n

# Set default active profile to 'dev'
spring.profiles.active=dev
```

Replace YOUR_NASA_API_KEY with your actual NASA API key. You can obtain an API key by registering at the [official NASA website](https://api.nasa.gov).


## Build and Run

Create new database
```bash
CREATE DATABASE nasa_asteroid;
```


Use Maven to build and run the application:

```bash
mvn clean package
cd target/
java -jar nasa-asteroids-api-0.0.1-SNAPSHOT.jar
```

The application will start and run on http://localhost:8080.

## Build and Run Using Docker

```bash
docker compose up --build -d
```


## API Endpoints

### Get Top 10 Closest Asteroids
- URL: /asteroids/closest?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
- Method: GET
- Path Parameters:
  - startDate (string): The start date of the range in yyyy-MM-dd format. 
  - endDate (string): The end date of the range in yyyy-MM-dd format. Maximum 7 days after startDate.
- Response:
  - 200 OK: The top 10 closest asteroids within the specified date range. 
  - 400 Bad Request: If the request is malformed. 
  - 500 Internal Server Error: For any other errors.


Example Request:
```bash
GET /asteroids/closest?startDate=2024-05-10&endDate=2024-05-17
```

Example Response:
```json
{
  "asteroids": [
    {
      "id": "3542519",
      "name": "(2010 PK9)",
      "absoluteMagnitude": 21.81,
      "estimatedDiameterMin": 0.1154928176,
      "estimatedDiameterMax": 0.258249791,
      "missDistanceKilometers": 6664518.761844655,
      "potentiallyHazardousAsteroid": false
    },
    {
      "id": "54442111",
      "name": "(2024 JY16)",
      "absoluteMagnitude": 25.028,
      "estimatedDiameterMin": 0.026239465,
      "estimatedDiameterMax": 0.0586732275,
      "missDistanceKilometers": 336224.399259631,
      "potentiallyHazardousAsteroid": false
    }
  ]
}
```


### Get Asteroid by ID
- URL: /asteroids/{id}
- Method: GET
- Path Parameter:
    - id (string): The ID of the asteroid.
- Response:
    - 200 OK: The top 10 closest asteroids within the specified date range.
    - 400 Bad Request: If the request is malformed.
    - 500 Internal Server Error: For any other errors.


Example Request:
```bash
GET /asteroids/3542519
```



Example Response:
```json
{
  "asteroidModel": [
    {
      "id": "3542519",
      "name": "(2010 PK9)",
      "absoluteMagnitude": 21.81,
      "estimatedDiameterMin": 0.1154928176,
      "estimatedDiameterMax": 0.258249791,
      "missDistanceKilometers": 6664518.761844655,
      "potentiallyHazardousAsteroid": false
    }
  ]
}
```

### Count Asteroids By Minimum Distance
- URL: /asteroids/count-by-distance?distance=minDistance
- Method: GET
- Request Body:
  - distance (long): The minimum distance of asteroids in Kilometers.
- Response:
  - 200 OK: The count of asteroids within more than specified distance.
  - 400 Bad Request: If the request is malformed.
  - 500 Internal Server Error: For any other errors.


Example Request:
```bash
GET /asteroids/count-by-distance?distance=1000000
```

Example Response:
```json
{
    "count": 18
}
```

### Database Storage of Top 10 Closest Asteroids
- URL: /asteroids/store-top10?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
- Method: POST
- Path Parameter:
  - startDate (string): The start date of the range in yyyy-MM-dd format.
  - endDate (string): The end date of the range in yyyy-MM-dd format. Maximum 7 days after startDate.
- Response:
  - 200 OK: The top 10 closest asteroids within the specified date range.
  - 400 Bad Request: If the request is malformed.
  - 500 Internal Server Error: For any other errors.


Example Request:
```bash
GET /asteroids/store-top10?startDate=2023-05-10&endDate=2023-05-17
```


Example Response:
```json
{
  "status": 200,
  "message": "Successfully saved top 10 closest asteroids to the database",
  "details": "Your database is updated"
}
```


## Exception Handling

The application uses a global exception handler to return structured error responses.

### Custom Exceptions
- AsteroidNotFoundException: Thrown when an asteroid with the specified ID is not found.

### Global Exception Handler
- Handles all exceptions and returns structured JSON responses.

Example Error Response:
```json
{
    "status": 404,
    "message": "Asteroid with ID 3542519 not found",
    "details": "Asteroid not found with the provided ID"
}
```


## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.