FROM maven:3.6.0-jdk-8-alpine as maven
COPY . /
WORKDIR /
RUN mvn package
FROM openjdk:8-jdk-alpine
EXPOSE 8080
COPY --from=maven target/criteria-transformer-api-0.0.1-SNAPSHOT.jar /target/app.jar
CMD ["java", "-jar", "/target/app.jar"]
