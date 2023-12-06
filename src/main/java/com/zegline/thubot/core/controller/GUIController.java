/**
 * @file GUIController.java
 * @brief Controller for handling GUI-related requests
 *
 * This controller is responsible for serving the main index page and providing
 * the necessary data to the view by fetching information from the /actuator/info endpoint
 */
package com.zegline.thubot.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @class GUIController
 * @brief Controller to manage GUI endpoints
 *
 * Handles web GUI requests and integrates backend information with the frontend views.
 * It mainly provides methods to render the index page with data fetched from application
 * actuator information.
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

    @GetMapping("/database/display")
    public String getDN() {
        return "explore_nodes";
    }

    @GetMapping("/database/form")
    public String getDBEntry(Model model){
        String jsonData = infoEndpoint.info().get("git").toString();

        model.addAttribute("commitid", jsonData);

        return "databaseForm";
    }

    @GetMapping("/login")
    String login(Model model) {
        // Fetch JSON data from /actuator/info
        String jsonData = infoEndpoint.info().get("git").toString();

        model.addAttribute("commitid", jsonData);
        return "login";
    }

    @GetMapping("/register")
    String register(Model model) {
        // Fetch JSON data from /actuator/info
        String jsonData = infoEndpoint.info().get("git").toString();

        model.addAttribute("commitid", jsonData);
        return "register";
    }

}
