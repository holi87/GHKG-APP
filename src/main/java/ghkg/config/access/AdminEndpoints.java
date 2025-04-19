package ghkg.config.access;

import ghkg.config.ApiPaths;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

public class AdminEndpoints {
    public static void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers("/actuator/**").hasRole("ADMIN");
        auth.requestMatchers(HttpMethod.POST, ApiPaths.ADMIN + "/users").hasRole("ADMIN");
        auth.requestMatchers(HttpMethod.GET, ApiPaths.ADMIN + "/users").hasRole("ADMIN");
    }
}
