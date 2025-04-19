package ghkg.config;

import ghkg.config.access.AdminEndpoints;
import ghkg.config.access.AuthenticatedEndpoints;
import ghkg.config.access.PublicEndpoints;
import ghkg.config.access.TripEndpoints;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

public class AccessRules {

    public static void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        PublicEndpoints.configure(auth);
        AdminEndpoints.configure(auth);
        TripEndpoints.configure(auth);
        AuthenticatedEndpoints.configure(auth);
    }

}
