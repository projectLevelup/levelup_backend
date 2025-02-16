package com.sparta.levelup_backend.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v2")
public class AuthFormController {

    @GetMapping("/signin")
    public String loginPage(HttpServletRequest request){
        return "signin";
    }

    @GetMapping("/signup")
    public String signUpUserPage(){

        return "signup";
    }


}
