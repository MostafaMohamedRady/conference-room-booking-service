# Conference Room Booking API

## Introduction
This is a REST API for booking conference rooms within a company.

## Features

- Book a meeting room.
- List available meeting room.

## Technologies Used
- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Swagger for API documentation

## Setup Instructions

#### IDE
   > you can run the application using intellij
   > import the app and run [ConferenceRoomBookingServiceApplication.java](src%2Fmain%2Fjava%2Fcom%2Fmashreq%2Fbooking%2FConferenceRoomBookingServiceApplication.java)

#### DOCKER
   ````shell
    docker build --tag=barka-application:latest
    docker run -p8081:8080 barka-application:latest
   ````

#### Maven
   ````shell
    git clone <repository-url>
    cd conference-room-booking
    mvn clean install
    mvn spring-boot:run
   ````
#### Running Tests
   ```sh
   mvn test
   ```

#### H2 Database console:
   ```sh
    http://localhost:8080/h2-console
    
    JDBC URL: jdbc:h2:mem:testdb
    User Name: sa
    Password: sa
   ```
#### Swagger UI for API documentation:
   ```sh
   http://localhost:8080/swagger-ui/index.html
   ```

## Project Structure
````
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── mashreq/
│   │           └── booking/
│   │               ├── ConferenceRoomBookingApplication.java
│   │               ├── config/
│   │               │   └── SwaggerConfig.java
│   │               ├── controller/
│   │               │   └── BookingController.java
│   │               ├── dto/
│   │               │   └── BookingRequest.java
│   │               ├── exception/
│   │               │   ├── GlobalExceptionHandler.java
│   │               │   ├── InvalidBookingRequestException.java
│   │               │   └── RoomNotAvailableException.java
│   │               │   └── ErrorMessage.java
│   │               ├── entity/
│   │               │   ├── BookingEntity.java
│   │               │   └── ConferenceRoomEntity.java
│   │               ├── repository/
│   │               │   └── BookingRepository.java
│   │               └── service/
│   │                   └── BookingService.java
│   ├── resources/
│   │   ├── db/
│   │   │   └── changelog/
│   │   │       └── db.changelog-master.yaml
│   │   ├── application.properties
│   │   └── logback-spring.xml
├── test/
│   └── java/
│       └── com/
│           └── yourcompany/
│               └── conferenceroombooking/
│                   └── BookingControllerTest.java
└── README.md
````

## Sequence Diagram

```mermaid
sequenceDiagram
FE->BE: signup 
Note right of BE: request validation 
BE-->FE: user 
FE->>BE: login
Note right of BE: validate username\npassword 
BE-->FE: token 
FE->BE: create account 
Note right of BE: validate acount details 
BE->DB:save account
BE-->FE: accountNo 
FE->BE: money-deposit
BE->BE:check-account-active
BE->DB:topup account balance \n save transaction
BE-->FE: reference number 
FE->BE: money-withdrwal
BE->BE:check-account-active \n check-balance
BE->DB:deduct from account balance \n save transaction
BE-->FE: reference number 
FE->BE: local-transfer
BE->BE:check-from-account-active
BE->BE:check-from-account-balance
BE->DB:deduct from account balance 
BE->DB:save from account transaction 
BE->BE:check-to-account-active
BE->DB:topup to account balance 
BE->DB:save to account transaction 
BE-->FE: reference number 
```

## API Endpoints

#### Postman Collection
[postman_collection.zip](https://github.com/MostafaMohamedRady/barak-banking/files/8599021/baraka-application.postman_collection.json.zip)

#### Book a Room

URL :-
````
http://localhost:8080/internal/v1/bookings
````
Request :-
````
{
  "attendees": 1,
  "startTime": "05:50",
  "endTime": "11:51"
}
````

Response :- 200
````
{
  "id": 1,
  "roomName": "Amaze",
  "date": "2024-08-12",
  "startTime": "05:50:00",
  "endTime": "11:51:00",
  "attendees": 1
}
````
Response:- validation error
````
{
  "statusCode": 400,
  "timestamp": "2024-08-12T19:34:00.558+00:00",
  "message": "[FieldName:-attendees, ErrorMessage:-Number of attendees must be at least 1 ==== ]",
  "description": "VALIDATION_ERROR"
}
````
Response:- start time / endtime error formate
````
{
  "statusCode": 400,
  "timestamp": "2024-08-12T19:34:49.550+00:00",
  "message": "[FieldName:-startTime, ErrorMessage:-Start time must be in the format HH:mm]",
  "description": "VALIDATION_ERROR"
}
````

Response:- No available room
````
{
  "statusCode": 404,
  "timestamp": "2024-08-12T20:14:50.720+00:00",
  "message": "No available room for the specified time and attendees.",
  "description": "NOT_FOUND_ERROR"
}
````

#### Get Available Bookings
URL:-
```
http://localhost:8080/internal/v1/bookings?startTime=02%3A10&endTime=11%3A00
```
Response:-
```
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbEBnbWFpbC5jb20iLCJleHAiOjE2NTExNTUyODUsImlhdCI6MTY1MTEzNzI4NX0.9tdTwnWIq4vUrz08HL2SiuCMDhQaOd2BDgdoUryZKurPnX4aDObOc4MebRLAHWoEZ1FysBleYhVVttzVh_Ej3Q"
}
```
```
{
  "statusCode": 401,
  "timestamp": "2022-04-28T09:48:02.785+00:00",
  "message": "Customer not found with email: string",
  "description": "UNAUTHORIZED_ERROR"
}
```
### End


