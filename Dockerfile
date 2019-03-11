FROM maven:3.6.0-jdk-8-alpine as maven
COPY . /
WORKDIR /
RUN mvn dependency:go-offline -B
RUN mvn package
FROM openjdk:8u171-jre-alpine
EXPOSE 8082
COPY --from=maven target/criteria-transformer-api-*.jar /target/app.jar
CMD ["java", "-jar", "/target/app.jar"]
