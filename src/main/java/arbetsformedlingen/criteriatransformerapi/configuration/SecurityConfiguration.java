package arbetsformedlingen.criteriatransformerapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChainTest(ServerHttpSecurity httpSecurity) {
        return httpSecurity.authorizeExchange().anyExchange().permitAll()
                .and()
                .httpBasic().and()
                .cors()
                .and()
                .csrf().disable()
                .build();
    }
}
