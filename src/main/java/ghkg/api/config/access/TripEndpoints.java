package ghkg.api.config.access;

import ghkg.api.config.ApiPaths;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

public class TripEndpoints {
    public static void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(HttpMethod.GET, ApiPaths.TRIPS).hasAnyRole("USER", "WORKER", "ADMIN");
        auth.requestMatchers(HttpMethod.GET, ApiPaths.TRIPS + "/{id}").hasRole("USER");
        auth.requestMatchers(HttpMethod.POST, ApiPaths.TRIPS).hasAnyRole("WORKER", "ADMIN");
        auth.requestMatchers(HttpMethod.PUT, ApiPaths.TRIPS + "/{id}").hasAnyRole("WORKER", "ADMIN");
        auth.requestMatchers(HttpMethod.DELETE, ApiPaths.TRIPS + "/{id}").hasRole("ADMIN");
    }
}
