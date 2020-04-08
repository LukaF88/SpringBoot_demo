# Bus-Stops (Turin)

Bus-Stops (Turin) is a web server written in Java that provides real time information about bus timetable for every line in Turin (Italy)
Currently, the project does not include an user interface

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- Java 7
- Maven

### Installing

To install and run:
```
mvn clean install
java -jar .\bus-stops-0.0.1-SNAPSHOT.jar
```

Server will be listening on [http://localhost:8081](http://localhost:8081)

Example: http://localhost:8081/541
Retrieves next bus for the stop id 541

## Running the tests

-- COMING SOON --

## Deployment

Bus-Stops (Turin) runs on every environment as long as it ha java v 7 or greater installed

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Boot](https://spring.io/projects/spring-boot) - Used for configuration and embedded Tomcat server

## Versioning

We are currently in ALPHA version of the project

## License

This project is licensed under the MIT License

## Acknowledgments

* [GTT - Gruppo Torinese Trasporti](http://www.gtt.to.it/cms/)