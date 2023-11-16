package com.zegline.thubot.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class handling GUI-related operations and serving the index page.
 */
@Controller
public class GUIController {

    @Autowired
    private InfoEndpoint infoEndpoint;

    @GetMapping("/")
    public String getIndex(Model model) {
        // Fetch JSON data from /actuator/info
        String jsonData = infoEndpoint.info().get("git").toString();

        // Add the JSON data to the model
        model.addAttribute("jsonData", jsonData);

        return "index";
    }

    @GetMapping("/dialognode")
    public String getDN() {
        return "explore_nodes";
    }

}
