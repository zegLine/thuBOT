/**
 * @file UserController.java
 * @brief Controller for handling User-related requests
 *
 * This controller handles the creation of User entities. However, it is meant only for testing
 * and should not be included in a staging or production deployment.
 */
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

/**
 * @class UserController
 * @brief Controller class for User-related actions
 *
 * Provides the REST endpoints for manipulating User entities. This class is only intended for
 * testing purposes and should not be used in a staging or production environment.
 */
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
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User createdUser = ur.save(user);
        createdUser.setPassword(null);
        return ResponseEntity.ok(createdUser);
    }

}
