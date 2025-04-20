package ghkg.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class TripsPageController {

    @GetMapping("/trips")
    public String adminPage() {
        return "trips";
    }
}
