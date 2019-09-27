package is.hi.hbv501.pirategame.pirategame;

import org.springframework.web.bind.annotation.RequestMapping;

public class HomeController {
    @RequestMapping("/")
    public String Home(){
        return "index";
    }
}
