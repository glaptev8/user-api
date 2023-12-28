FROM openjdk:21-jdk-slim-buster
WORKDIR /user-api

COPY build/libs/user-api-1.0-SNAPSHOT.jar /user-api/build/

WORKDIR /user-api/build

EXPOSE 8082

ENTRYPOINT java -jar user-api-1.0-SNAPSHOT.jar