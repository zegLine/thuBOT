/*
* POC (Proof of concept) implementation of question matching using the OpenAI API
* using HttpURLConnection (net.java) for the request to OpenAI
* and Jackson (com.fasterxml) for parsing JSON
* */
package com.zegline.thubot.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zegline.thubot.core.service.openai.OpenAIService;

@RestController
@RequestMapping("/api/input")
public class UserInputController {

    @Value("${openai.api.key}") // Read the API key from your application.properties or application.yml file
    private String openaiApiKey;

    @GetMapping("/ask")
    public String input_ask(@RequestBody() Map<String, String> body) {
        String question = body.get("question");

        List<String> list_nodes = new ArrayList<>();

        return OpenAIService.getQuestionMatch(question, list_nodes).get(0);
    }

    
}
