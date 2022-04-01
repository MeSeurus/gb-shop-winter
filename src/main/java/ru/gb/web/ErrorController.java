package ru.gb.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errors")
public class ErrorController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "errors/access-denied";
    }
}