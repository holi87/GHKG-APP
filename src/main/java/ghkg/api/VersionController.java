package ghkg.api;

import ghkg.config.ApiPaths;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(ApiPaths.VERSION)
@RequiredArgsConstructor
@Tag(name = "Version", description = "Application version and metadata info")
public class VersionController {

    private final GitProperties gitProperties;

    @GetMapping
    public Map<String, String> getVersion() {
        return Map.of(
                "version", gitProperties.get("build.version"),
                "commit", gitProperties.get("commit.id.abbrev"),
                "branch", gitProperties.get("branch"),
                "time", gitProperties.get("commit.time")
        );
    }
}