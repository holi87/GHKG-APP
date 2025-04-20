package ghkg.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

@ControllerAdvice
public class AdminController {

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}
