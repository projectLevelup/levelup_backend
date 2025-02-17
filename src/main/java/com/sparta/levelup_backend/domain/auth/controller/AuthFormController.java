package com.sparta.levelup_backend.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthFormController {

    @GetMapping("/v2/signin")
    public String loginPage(HttpServletRequest request){
        return "signin";
    }


}
