/**
 * @file UserInputController.java
 * @brief Controller for handling user input related requests.
 *
 * This controller is responsible for processing user input and retrieving appropriate
 * responses from the DialogNodeMatch service.
 */
package com.zegline.thubot.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.service.dialogNodeMatch.DialogNodeMatch;

/**
 * @class UserInputController
 * @brief Controller to manage user input related actions.
 *
 * Provides an API endpoint to receive user input and return a response. The response is
 * either matched from dialog nodes or queried from OpenAI service.
 */
@RestController
@RequestMapping("/api/input")
public class UserInputController {

    /**
     * The OpenAI API key read from the application properties or the application.yml file.
     */
    @Value("${openai.api.key}")
    private String openaiApiKey;

    /**
     * The service to match dialog nodes and interface with the OpenAI service.
     */
    @Autowired
    private DialogNodeMatch dialogNodeMatchService;

    /**
     * Endpoint to receive user input and retrieve a response node based on the provided input.
     *
     * @param userInput The user input received as a request parameter.
     * @return The DialogNode instance that responds to the user input.
     */
    @GetMapping("/ask")
    public DialogNode inputAsk(@RequestParam String userInput) {
        return dialogNodeMatchService.getResponseNode(userInput);
    }
}