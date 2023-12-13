/**
 * @file GUIController.java
 * @brief Controller for handling GUI-related requests
 *
 * This controller is responsible for serving the main index page and providing
 * the necessary data to the view by fetching information from the /actuator/info endpoint
 */
package com.zegline.thubot.core.controller;

import com.zegline.thubot.core.model.security.User;
import com.zegline.thubot.core.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private UserRepository ur;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public String getDBEntry(Model model, @AuthenticationPrincipal UserDetails userDetails){
        String jsonData = infoEndpoint.info().get("git").toString();

        model.addAttribute("commitid", jsonData);
        model.addAttribute("loggedInUser", userDetails.getUsername());

        return "databaseForm";
    }

    @GetMapping("/login")
    public String login(Model model) {
        // Fetch JSON data from /actuator/info
        String jsonData = infoEndpoint.info().get("git").toString();

        model.addAttribute("commitid", jsonData);
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // Fetch JSON data from /actuator/info
        String jsonData = infoEndpoint.info().get("git").toString();

        model.addAttribute("commitid", jsonData);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(Model model, @RequestParam Map<String, String> body) {
        List<String> errors = new ArrayList<>();

        // Fetch JSON data from /actuator/info and add git info to frontend
        String jsonData = infoEndpoint.info().get("git").toString();
        model.addAttribute("commitid", jsonData);

        String submittedUsername = body.get("username");
        String submittedPassword = body.get("password");
        String submittedPasswordConfirmed = body.get("password_confirm");

        // Check password with confirmed
        if (!submittedPassword.equals(submittedPasswordConfirmed)) {
            errors.add("Passwords do not match. Please try again.");
        }

        // Check if user exists
        if (ur.existsByUsername(submittedUsername)) {
            errors.add("User with this username already exists");
        }

        if (!errors.isEmpty()) {
            // There are errors, add them to the model and return to the registration page
            model.addAttribute("errors", errors);
        } else {
            // Everything is OK (no errors) and we can add user in the db
            User u = new User();
            u.setUsername(submittedUsername);
            u.setPassword(passwordEncoder.encode(submittedPassword));
            ur.save(u);
            model.addAttribute("message", "Registration was successful. Please log in");
        }

        return "register";
    }

}
