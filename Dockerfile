FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
RUN mvn clean package
ARG JAR_FILE=target/criteria-transformer-api-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} criteria-transformer-api.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/criteria-transformer-api.jar"]
