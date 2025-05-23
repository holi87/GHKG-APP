package ghkg.web.pages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class AdminPageController {

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}
