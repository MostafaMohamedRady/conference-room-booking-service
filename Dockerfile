FROM openjdk:11-jre-slim
MAINTAINER mostafa
COPY target/barka-application-0.0.1-SNAPSHOT.jar barka-application-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/barka-application-0.0.1-SNAPSHOT.jar"]