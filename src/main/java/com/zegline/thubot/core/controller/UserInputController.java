package com.zegline.thubot.core.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zegline.thubot.core.service.dialogNodeMatch.dialogNodeMatch;

@RestController
@RequestMapping("/api/input")
public class UserInputController {

    @Value("${openai.api.key}") // Read the API key from your application.properties or application.yml file
    private String openaiApiKey;

    @GetMapping("/ask")
    public List<String> input_ask(@RequestParam String userInput, @RequestParam String parent_id) {
        String returnNodeStr = dialogNodeMatch.getResponseNode(userInput, parent_id);

        List<String> list_nodes = new ArrayList<>();

        
        // OpenAIService.getQuestionMatch(question, list_nodes).get(0);
        return new ArrayList<String>();
    }

    
}
