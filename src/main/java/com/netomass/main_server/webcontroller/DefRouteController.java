package com.netomass.main_server.webcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DefRouteController {

    @GetMapping
    public String path(){
        return "redirect:/web/pages/op/home";
    }
}
