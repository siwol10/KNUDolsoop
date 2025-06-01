package com.example.SpringBoot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(){
        return "index";
    }

    @GetMapping("/intro")
    public String intro(){
        return "intro";
    }

    @GetMapping("/careInfo")
    public String careInfo(){
        return "careInfo";
    }

}
