package ghkg.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

public class AccessRules {

    public static void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        // Public endpoints
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

        // Admin-only endpoints
        auth.requestMatchers("/actuator/**").hasRole("ADMIN");
        auth.requestMatchers(HttpMethod.POST, ApiPaths.ADMIN + "/users").hasRole("ADMIN");
        auth.requestMatchers(HttpMethod.GET, ApiPaths.ADMIN + "/users").hasRole("ADMIN");

        // Trips – different roles based on an HTTP method
        auth.requestMatchers(HttpMethod.GET, ApiPaths.TRIPS + "/**").hasAnyRole("USER", "WORKER", "ADMIN");
        auth.requestMatchers(ApiPaths.TRIPS + "/**").hasAnyRole("WORKER", "ADMIN");

        // All other requests
        auth.anyRequest().authenticated();
    }
}
