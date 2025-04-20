package ghkg.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MePageController {

    @GetMapping("/me")
    public String mePage() {
        return "me";
    }
}
