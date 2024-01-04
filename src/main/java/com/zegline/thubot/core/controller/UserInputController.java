/**
 * @file UserInputController.java
 * @brief Controller for handling user input related endpoints
 *
 * This controller is responsible for processing user input and returning appropriate
 * responses by interfacing with the DialogNodeMatch service.
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

//import com.zegline.thubot.core.service.dialogNodeMatch.DialogNodeMatch;


/**
 * @class UserInputController
 * @brief Controller to manage user input related actions
 *
 * Provides an API endpoint to receive user input and return a list of possible responses
 * by matching with dialog nodes or querying the OpenAI service
 */
@RestController
@RequestMapping("/api/input")
public class UserInputController {

    @Value("${openai.api.key}") // Read the API key from your application.properties or application.yml file
    private String openaiApiKey;

    /**
     * Endpoint to handle user input and retrieve a response node based on the provided input and parent ID.
     *
     * @param userInput The user input sent as a request parameter.
     * @param parent_id The parent ID associated with the user input.
     * @return A list of strings containing response nodes associated with the user input and parent ID.
     */
    @Autowired
    private DialogNodeMatch dialogNodeMatchService;

    @GetMapping("/ask")
    public DialogNode inputAsk(@RequestParam String userInput) {
        return dialogNodeMatchService.getResponseNode(userInput);
    }
    
}