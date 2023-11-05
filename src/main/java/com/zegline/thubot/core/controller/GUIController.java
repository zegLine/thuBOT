package com.zegline.thubot.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GUIController {

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

}
