package com.zegline.thubot.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.repository.DialogNodeRepository;
import com.zegline.thubot.core.service.dialogNodeMatch.dialogNodeMatch;
import com.zegline.thubot.core.service.openai.OpenAIService;

@RestController
@RequestMapping("/api/input")
public class UserInputController {

    @Value("${openai.api.key}") // Read the API key from your application.properties or application.yml file
    private String openaiApiKey;

    @GetMapping("/ask")
    public List<String> input_ask(@RequestParam String userInput, @RequestParam String currentContext) {
        String returnNodeStr = dialogNodeMatch.getResponseNode(userInput, currentContext);

        List<String> list_nodes = new ArrayList<>();

        
        // OpenAIService.getQuestionMatch(question, list_nodes).get(0);
        return new ArrayList<String>();
    }

    
}
