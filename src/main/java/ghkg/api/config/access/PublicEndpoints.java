package ghkg.api.config.access;

import ghkg.api.config.ApiPaths;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

public class PublicEndpoints {
    public static void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(
                ApiPaths.AUTH + "/login",
                "/h2-console/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/actuator/health",
                "/actuator/info",
                ApiPaths.VERSION,
                ApiPaths.CARS + "/**"
        ).permitAll();
    }
}
