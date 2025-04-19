package ghkg.config.access;

import ghkg.config.ApiPaths;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

public class AdminEndpoints {
    public static void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers("/actuator/**").hasRole("ADMIN");
        auth.requestMatchers(ApiPaths.ADMIN + "/**").hasRole("ADMIN");

    }
}
