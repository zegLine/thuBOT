/**
 * @file UserInputController.java
 * @brief Controller for handling user input related endpoints
 *
 * This controller is responsible for processing user input and returning appropriate
 * responses by interfacing with the DialogNodeMatch service.
 */
package com.zegline.thubot.core.controller;

import com.zegline.thubot.core.repository.DialogNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.service.DialogNode.DialogNodeMatch;

import java.util.Objects;
import java.util.Optional;

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
     * Endpoint to handle user input and retrieve a response node based on the provided input and parent ID.
     *
     * @param userInput The user input sent as a request parameter.
     * @param parent_id The parent ID associated with the user input.
     * @return A list of strings containing response nodes associated with the user input and parent ID.
     */
    @Autowired
    private DialogNodeMatch dialogNodeMatchService;

    @Autowired
    private DialogNodeRepository dnr;

    /**
     * Endpoint to receive user input and retrieve a response node based on the provided input.
     *
     * @param userInput The user input received as a request parameter.
     * @return The DialogNode instance that responds to the user input.
     */
    @GetMapping("/ask")
    public DialogNode inputAsk(@RequestParam String userInput, @RequestParam String currentNodeId) {
        DialogNode currentNode = null;
        if (!Objects.equals(currentNodeId, "")) {
            Optional<DialogNode> currentNodeOpt = dnr.findById(currentNodeId);
            if (currentNodeOpt.isPresent()) currentNode =currentNodeOpt.get();
        }
        return dialogNodeMatchService.getResponseNode(userInput, currentNode);
    }
}