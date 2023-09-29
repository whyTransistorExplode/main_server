package com.netomass.main_server.webcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.netomass.main_server.constants.TemplatePath.*;
import static com.netomass.main_server.constants.URLPath.*;

@Controller
@RequestMapping(WEB_PAGES_PV)
public class StorageRouteWeb {

    @GetMapping(STORAGE)
    public String storagePage(){
        return PAGES_USER_STORAGEPAGE;
    }
}
