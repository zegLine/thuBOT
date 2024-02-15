/**
 * @file OpenAIService.java
 * @brief Service class for interactions with the OpenAI API.
 *
 * This class connects to the OpenAI API, sends HTTP POST requests, and processes the responses. 
 * It follows the API's specified format for requests and handles the response parsing and error handling.
 */
package com.zegline.thubot.core.service.openai;

import org.springframework.beans.factory.annotation.Value;
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

/**
 * @class OpenAIService
 * @brief Service class for OpenAI API interaction.
 *
 * Responsible for interacting with the OpenAI API. 
 * Constructs HTTP POST requests to the API, sends the requests, and processes the API response. 
 */
@Service
public class OpenAIService {
    
    @Value("${openai.api.key}") 
    private String openaiApiKey;
    
    /**
     * Takes a user provided question and a list of chat node questions then gets the question that best matches the user's question from the OpenAI API.
     *
     * @param input_question The user's question.
     * @param list_nodes A list of possible response questions.
     * 
     * @return A list containing the best matching question.
     * 
     * @throws Exception Throws an exception if there was a problem with the API request.
     */
     public List<String> getQuestionMatch(String input_question, List<String> list_nodes) {

        List<String> responseList = new ArrayList<>();
        StringBuilder openaiInput = new StringBuilder("You will match the user input to one of the following questions:");

        for (String node : list_nodes) {
            openaiInput.append("QUESTION").append(list_nodes.indexOf(node)).append(":").append(node).append(";");
        }

        openaiInput.append("You will ONLY respond with the Question NUMBER that you think MAKES SENSE to match to the input. DO NOT RESPOND WITH ANYTHING ELSE THAN QUESTIONX (where X is the number). Check those marked with CHKFRST first");

        try {
            
            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + openaiApiKey);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [" +
                    "{\"role\": \"system\", \"content\": \"" + openaiInput + "\"}," +
                    "{\"role\": \"user\", \"content\": \"" + input_question + "\"}" +
                    "], \"temperature\": 0.4, \"max_tokens\": 256, \"top_p\": 1, \"frequency_penalty\": 0, \"presence_penalty\": 0}";

            try (DataOutputStream os = new DataOutputStream(connection.getOutputStream())) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    String jsonResponse = response.toString();
                    System.out.println(jsonResponse);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(jsonResponse);
                    String finalResponse = root.get("choices").get(0).get("message").get("content").toString();

                    if(finalResponse != "null") {
                        responseList.add(finalResponse);
                    }
                    return responseList;
                }
            } else {
                
                System.err.println("HTTP Request failed with response code: " + responseCode);
                System.out.println(requestBody);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseList;
    }
}