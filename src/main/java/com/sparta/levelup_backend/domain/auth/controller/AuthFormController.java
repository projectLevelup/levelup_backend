package com.sparta.levelup_backend.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/v2")
public class AuthFormController {

    @GetMapping("/signin")
    public String signInPage(HttpServletRequest request){
        return "signin";
    }

    @GetMapping("/signup")
    public String signUpUserPage(){

        return "signup";
    }


}
