package ghkg.api.config;

import ghkg.api.config.access.AdminEndpoints;
import ghkg.api.config.access.AuthenticatedEndpoints;
import ghkg.api.config.access.PublicEndpoints;
import ghkg.api.config.access.TripEndpoints;
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
