package com.netomass.main_server.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

//    @RequestMapping(value = "/home")
//    public String index() {
//        return "/home";
//    }


    @GetMapping(value = "")
    public String redirectHome() {
        return "redirect:/home";
    }


    @GetMapping(value="/home")
    public String homePage(){
        return "pages/home/homepage.html";
    }




}
