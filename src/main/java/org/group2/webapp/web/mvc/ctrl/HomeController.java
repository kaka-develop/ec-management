package org.group2.webapp.web.mvc.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping(value = {"/","/home"})
    public String index() {
        return "home";
    }

    @GetMapping("/login")
    public String signin() {
        return "login";
    }


}
