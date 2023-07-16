package com.netomass.main_server.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

@Controller
public class WebController {

//    @RequestMapping(value = "/home")
//    public String index() {
//        return "/home";
//    }


    @GetMapping(value = "")
    public String homePage() {
        return "redirect:/home";
    }


    @GetMapping(value="/home")
    public String meme1(HttpServletRequest request){
        System.out.println("||||||||||||||||||||||||||||||||||||");
        System.out.println("income request from: " + request.getRemoteAddr());
        System.out.println("||||||||||||||||||||||||||||||||||||");
        return "pages/home/homepage";
    }

    @RequestMapping(value = "/projector")
    public String meme2(HttpServletRequest request){
        System.out.println("||||||||||||||||||||||||||||||||||||");
        System.out.println("income request from: " + request.getContextPath());
        System.out.println("income request from: " + request.getPathInfo());
        System.out.println("income request from: " + request.getServletPath());
        System.out.println("||||||||||||||||||||||||||||||||||||");
//        model.addAttribute("text1", "hello world");
//        model.addAttribute("text2", "secure connection is not established!!!");

        return "intrig/page2";
    }


}
