FROM maven:3.6.0-jdk-8-alpine
VOLUME /tmp
EXPOSE 8080
COPY . ./
RUN mvn package
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/criteria-transformer-api.jar"]
