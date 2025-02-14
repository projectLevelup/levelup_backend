package com.sparta.levelup_backend.domain.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthFormController {

    @GetMapping("/v1/signin")
    public String loginPage(){

        return "signin";
    }


}
