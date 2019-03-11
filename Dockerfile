FROM maven:3.6.0-jdk-8-alpine as maven
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
RUN mvn package
FROM openjdk:8u171-jre-alpine
WORKDIR /criteria-transformer-api
COPY --from=maven target/criteria-transformer-api-*.jar ./
CMD ["java", "-jar", "./target/criteria-transformer-api.jar"]
