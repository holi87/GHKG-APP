package ghkg.web;

import ghkg.config.BuildVersionProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final BuildVersionProvider versionProvider;

    @ModelAttribute("version")
    public String version() {
        String version = versionProvider.getVersion();
        return version != null ? version : "N/A";
    }

    @ModelAttribute("pageTitle")
    public String defaultTitle() {
        return "GHKG App";
    }
}
