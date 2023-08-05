package com.netomass.main_server.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web/pages/op/")
@RequiredArgsConstructor
public class WebController {

//    @RequestMapping(value = "/home")
//    public String index() {
//        return "/home";
//    }



    @GetMapping(value = "")
    public String redirectHome() {
        return "redirect:/home";
    }


    @RequestMapping("/home")
    public String homePage(){

        return "pages/home/homepage";
    }

    @GetMapping("/self/portfolio")
    public String portfolioPage(){
        return "pages/portfolio/portfolio";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "pages/login/login.html";
    }

}
