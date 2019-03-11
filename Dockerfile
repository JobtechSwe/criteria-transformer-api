FROM maven:3.6.0-jdk-8-alpine as maven
COPY . /
WORKDIR /
RUN mvn dependency:go-offline -B
RUN mvn package
FROM openjdk:8-jdk-alpine
EXPOSE 8080
COPY --from=maven target/criteria-transformer-api-*.jar /target/app.jar
RUN chgrp -R 0 /target && \
    chmod -R g=u /target
CMD ["java", "-jar", "/target/app.jar"]
