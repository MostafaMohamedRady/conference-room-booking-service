FROM openjdk:11-jre-slim
MAINTAINER mostafa
COPY target/conference-room-booking-service-0.0.1-SNAPSHOT.jar conference-room-booking-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/conference-room-booking-service-0.0.1-SNAPSHOT.jar"]