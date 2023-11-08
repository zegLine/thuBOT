package com.zegline.thubot.core.service.dialogNodeMatch;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.service.openai.OpenAIService;

@Service
public class dialogNodeMatch {
    
    /**
     * Matches the user input with responses in the databases
     * @param userInput <b>String</b> The input string the user provided, could be the prompt or natural langauge.
     * @param parent <b>String</b> The current context the chatbot is in.
     * @return <b>String</b> of the fetched answer, "null" if doesn't exist.
     * 
     */
    public static String getResponseNode(String userInput, DialogNode parent){
        




        List<String> responseList;
        List<String> possibleResponses;
        responseList = OpenAIService.getQuestionMatch(userInput, possibleResponses);

        if(responseList.size()!=0){
            return responseList.get(0);
        }

        return "null";
    }

        
}
