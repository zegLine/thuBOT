/**
 * @file GUIController.java
 * @brief Controller that handles the requests related to GUI display and interactions
 *
 * This controller is primarily responsible for serving the GUI pages. It fetches data from
 * different services like the actuator information and provides it to the views.
 */
package com.zegline.thubot.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zegline.thubot.core.model.security.User;
import com.zegline.thubot.core.repository.UserRepository;

/**
 * @class GUIController
 * @brief Handles the requests related to GUI display and interactions
 *
 * Provides endpoints for rendering the GUI pages and populates the frontend views with
 * data fetched from backend services like application actuator information. It also
 * provides endpoints for user registration and login.
 */
@Controller
public class GUIController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InfoEndpoint infoEndpoint;

    /**
    * Serves the main index page and populates it with actuator information.
    *
    * @param model The model in which to place attributes for the view.
    * @return String representing name of the view for the main index page.
    */
    @GetMapping("/")
    public String getIndex(Model model) {
        // Fetch JSON data from /actuator/info
        String jsonData = infoEndpoint.info().get("git").toString();

        // Add the JSON data to the model
        model.addAttribute("jsonData", jsonData);

        return "index";
    }

    /**
    * End point to serve the database display page.
    *
    * @return String representing name of the view for database display page.
    */
    @GetMapping("/database/display")
    public String getDN() {
        return "explore_nodes";
    }

    /**
    * End point to serve the static database display page.
    *
    * @return String representing name of the view for static database display page.
    */
    @GetMapping("/database/display/static")
    public String getDN1() {
        return "explore_nodes_static";
    }

    /**
    * Serves the database form view.
    *
    * @param model The model in which to place attributes for the view.
    * @param userDetails The UserDetails object of the currently authenticated user.
    * @return Name of the view for the database form.
    */
    @GetMapping("/database/form")
    public String getDBEntry(Model model, @AuthenticationPrincipal UserDetails userDetails){
        String jsonData = infoEndpoint.info().get("git").toString();

        model.addAttribute("commitid", jsonData);
        model.addAttribute("loggedInUser", userDetails.getUsername());

        return "databaseForm";
    }

    /**
    * Serves the login view.
    *
    * @param model The model in which to place attributes for the view.
    * @return Name of the view for the login page.
    */
    @GetMapping("/login")
    public String login(Model model) {
        // Fetch JSON data from /actuator/info
        String jsonData = infoEndpoint.info().get("git").toString();

        model.addAttribute("commitid", jsonData);
        return "login";
    }

    /**
    * Serves the registration form view.
    *
    * @param model The model in which to place attributes for the view.
    * @return Name of the view for the registration form.
    */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // Fetch JSON data from /actuator/info
        String jsonData = infoEndpoint.info().get("git").toString();

        model.addAttribute("commitid", jsonData);
        return "register";
    }

    /**
    * Serves an access-denied view.
    *
    * @return Name of the view for the access-denied page.
    */
    @GetMapping("/access-denied")
    public String accessDeniedPage(){
        return "access-denied";
    }

    /**
    * Handles the user registration process, including error handling and successful registration messaging.
    *
    * @param model The model in which to place attributes for the view.
    * @param body The form data from the registration form.
    * @return Name of the view for the registration page (to be refreshed with new messages or errors).
    */
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
        if (userRepository.existsByUsername(submittedUsername)) {
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
            userRepository.save(u);
            model.addAttribute("message", "Registration was successful. Please log in");
        }

        return "register";
    }

}
