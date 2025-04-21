package ghkg.config;

import lombok.Getter;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
public class BuildVersionProvider {
    @Getter
    private final String version;

    public BuildVersionProvider(BuildProperties properties) {
        this.version = properties.getVersion();
    }

}