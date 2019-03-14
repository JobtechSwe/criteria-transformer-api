package arbetsformedlingen.criteriatransformerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class CriteriaTransformerApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(CriteriaTransformerApiApplication.class, args);
	}
}
