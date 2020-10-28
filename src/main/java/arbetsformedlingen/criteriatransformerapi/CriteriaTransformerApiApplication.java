package arbetsformedlingen.criteriatransformerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableWebFlux
public class CriteriaTransformerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CriteriaTransformerApiApplication.class, args);
    }

    /**
     * REDIRECT ROOT TO SWAGGER-UI
     */
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route(GET("/"), req ->
                ServerResponse.temporaryRedirect(URI.create("/swagger-ui/")).build());
    }
}
