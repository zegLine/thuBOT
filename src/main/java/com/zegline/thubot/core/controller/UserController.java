package com.zegline.thubot.core.controller;

import com.zegline.thubot.core.model.security.User;
import com.zegline.thubot.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository ur;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Route for creating a user
    // ONLY FOR TESTING
    // DO NOT DEPLOY IN STAGING EVER
    // TODO: REMOVE BEFORE DEPLOYING TO STAGING
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Hash the password
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Save the user with the hashed password
        User createdUser = ur.save(user);

        // Clear the password before returning the response for security reasons
        createdUser.setPassword(null);
        return ResponseEntity.ok(createdUser);
    }

}