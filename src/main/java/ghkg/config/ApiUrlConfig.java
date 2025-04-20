package ghkg.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiUrlConfig {

    private final Environment environment;

    @Getter
    private String apiUrl;

    @PostConstruct
    public void init() {
        String address = environment.getProperty("server.address", "localhost");
        String port = environment.getProperty("server.port", "8080");

        if ("0.0.0.0".equals(address)) {
            address = "localhost";
        }

        this.apiUrl = String.format("http://%s:%s", address, port);
    }
}
