package ghkg.config.access;

import ghkg.config.ApiPaths;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

// ku pamięci - w koncu trzeba to zrobic - trip ma dzialac na zasadzie ze geta moze miec kazdy user, a dodawac,edytowac, usuwac tylko worker lub admin

public class TripEndpoints {
    public static void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(HttpMethod.GET, ApiPaths.TRIPS + "/**").hasAnyRole("USER", "WORKER", "ADMIN");
        auth.requestMatchers(ApiPaths.TRIPS + "/**").hasAnyRole("WORKER", "ADMIN");
    }
}
