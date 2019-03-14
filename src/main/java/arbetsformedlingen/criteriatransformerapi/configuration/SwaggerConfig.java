package arbetsformedlingen.criteriatransformerapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig {

    public static final String BASE_PACKAGE = "arbetsformedlingen.criteriatransformerapi.controller";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(regex("/transform.*"))
                .build().pathMapping("/").apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Elise Criteria transformer API")
                .description("Transform elise criteria to sok-api criteria")
                .contact(new Contact("Platsbanken", null, "platsbanken@arbetsformedlingen.se"))
                .build();
    }

}
