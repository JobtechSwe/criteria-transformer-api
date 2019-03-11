FROM maven:3.6.0-jdk-8-alpine as maven
COPY . /
WORKDIR /
dockerRUN mvn package
FROM openjdk:8-jdk-alpine
EXPOSE 8080
COPY --from=maven target/criteria-transformer-api-*.jar /target/app.jar
RUN chmod -R 777 /target
USER 10000
CMD ["java", "-jar", "/target/app.jar"]
