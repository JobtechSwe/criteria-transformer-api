# Criteria Transformer API

### Requirements:

    Java 8
    Maven

### Build and run project:

    $ mvn clean package
    $ mvn spring-boot:run
    
### Swagger:

http://localhost:8080

[dev](https://develop-transformer.dev.services.jtech.se)

[stage](https://staging-transformer.dev.services.jtech.se)

[Prod](https://transformer.dev.services.jtech.se)

### Load testing with apache-bench
    abs -n 100 -c 5 -p criteria-body.json -T 'application/json' http://localhost:8080/transform
