package com.sparta.levelup_backend.domain.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthFormController {

    @GetMapping("/v2/signin")
    public String signInPage(){
        return "signin";
    }


}
