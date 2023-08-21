package com.netomass.main_server.webcontroller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.netomass.main_server.constants.TemplatePath.*;
import static com.netomass.main_server.constants.URLPath.*;

@Controller
@RequestMapping(WEB_PAGES_OP)
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


    @RequestMapping(HOME)
    public String homePage(){

        return PAGES_HOME_HOMEPAGE;
    }

    @GetMapping(SELF_PORTFOLIO)
    public String portfolioPage(){
        return PAGES_PORTFOLIO_PORTFOLIO;
    }

    @GetMapping(LOGIN)
    public String loginPage(){
        return PAGES_LOGIN_LOGIN;
    }

}
