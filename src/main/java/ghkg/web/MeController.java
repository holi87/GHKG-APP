package ghkg.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MeController {

    @GetMapping("/me.html")
    @PreAuthorize("isAuthenticated()")
    public String mePage() {
        return "me";
    }
}
