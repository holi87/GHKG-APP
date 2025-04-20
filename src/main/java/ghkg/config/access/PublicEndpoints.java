package ghkg.config.access;

import ghkg.config.ApiPaths;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

public class PublicEndpoints {
    public static void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(
                // System endpoints
                "/h2-console/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/actuator/health",
                "/actuator/info",

                // Public API endpoints
                ApiPaths.VERSION,
                ApiPaths.ROOT + "/login",
                ApiPaths.CARS + "/**",

                // Frontend routes and static resources
                "/",
                "/index",
                "/me.html",
                "/login",
                "/admin",
                "/garage",
                "/trips",
                "/css/**",
                "/js/**",
                "/images/**",
                "/favicon.ico",
                "/webjars/**"
        ).permitAll();
    }
}
