FROM openjdk:17-jdk-slim

WORKDIR /server

COPY ./target/cnpm-project-1.0-SNAPSHOT.jar server.jar

ENTRYPOINT ["java", "-jar", "server.jar"]
