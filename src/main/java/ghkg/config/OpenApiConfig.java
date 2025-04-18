package ghkg.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${build.version:unknown}")
    private String buildVersion;

    @Value("${git.commit.id.abbrev:unknown}")
    private String gitCommitId;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GH & KG API")
                        .description("API for testers")
                        .version(buildVersion + " (" + gitCommitId + ")"))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new Components().addSecuritySchemes("basicAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")));
    }
}