package com.springboot.Teamproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {

    @GetMapping("/")
    public String root(){

        return "redirect:/blog/list/";
    }
}
