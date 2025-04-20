package ghkg.web;

import ghkg.config.ApiUrlConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RestTemplate restTemplate;
    private final ApiUrlConfig apiUrlConfig;

    @GetMapping("/")
    public String index(Model model) {
        String actuatorInfoUrl = apiUrlConfig.getApiUrl() + "/actuator/info";
        @SuppressWarnings("unchecked")
        Map<String, Object> info = restTemplate.getForObject(actuatorInfoUrl, Map.class);
        @SuppressWarnings("unchecked")
        Object version = ((Map<String, Object>) info.get("build")).get("version");
        model.addAttribute("version", version != null ? version : "N/A");
        return "index";
    }
}
