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
     * @param parent_id <b>String</b> The id of the parent node.
     * @return <b>String</b> of the fetched answer, "null" if doesn't exist.
     * 
     */
    public static String getResponseNode(String userInput, String parent_id){
        
        //First match with one node in the database, and if not found, send the user input and the 
        // subtree and the current parent text to openAI



        List<String> responseList;
        List<String> possibleResponses;
        //responseList = OpenAIService.getQuestionMatch(userInput, possibleResponses);

        //if(responseList.size()!=0){
        //    return responseList.get(0);
       //}

        return "null";
    }

        
}
