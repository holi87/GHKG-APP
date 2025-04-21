package ghkg.web.pages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomePageController {


    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
}
