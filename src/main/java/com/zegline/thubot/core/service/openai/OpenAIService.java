/**
 * @file OpenAIService.java
 * @brief Service class for interactions with the OpenAI API
 *
 * This class is responsible for making requests to the OpenAI API with the user input
 * and fetching a response based on the list of possible questions provided. It handles
 * the construction of the request, communication with the API, and parsing of the response
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
 * @brief Service class for querying the OpenAI API
 *
 * The OpenAIService class encapsulates the functionality required to interact with the OpenAI API.
 * It performs HTTP POST requests to send the user input and processes the response to find the best-matching
 * question node
 */
@Service
public class OpenAIService {
    
    @Value("${openai.api.key}") // Read the API key from your application.properties or application.yml file
    private static String openaiApiKey;
    /**
     * Sends user input to the OpenAI API and receives a matched response.
     * This method constructs a request with the user's input and a list of possible questions,
     * sends it to the OpenAI API, and parses the response to extract the question that best matches the input
     *
     * @param input_question Natural language user input
     * @param list_nodes List of questions to match the input against
     * @return A list containing the best-matched question node or an empty list if no match is found
     */
    public static List<String> getQuestionMatch(String input_question, List<String> list_nodes) {

        List<String> responseList = new ArrayList<>();

       // Prepare the input for the OpenAI API
        StringBuilder openaiInput = new StringBuilder("You will match the user input to one of the following questions:");
        for (String node : list_nodes) {
            openaiInput.append("QUESTION").append(list_nodes.indexOf(node)).append(":").append(node).append(";");
        }
        openaiInput.append("You will ONLY respond with the Question NUMBER that you think MAKES SENSE to match to the input");

        try {
            // Create a URL object for the OpenAI API endpoint
            URL url = new URL("https://api.openai.com/v1/chat/completions");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Set request headers
            connection.setRequestProperty("Authorization", "Bearer " + openaiApiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // Enable input/output streams for the connection
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Construct the request body
            String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [" +
                    "{\"role\": \"system\", \"content\": \"" + openaiInput + "\"}," +
                    "{\"role\": \"user\", \"content\": \"" + input_question + "\"}" +
                    "], \"temperature\": 0.4, \"max_tokens\": 256, \"top_p\": 1, \"frequency_penalty\": 0, \"presence_penalty\": 0}";

            // Write the request body to the connection
            try (DataOutputStream os = new DataOutputStream(connection.getOutputStream())) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response code from the API
            int responseCode = connection.getResponseCode();

            // Read the response from the API
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // Parse the response and extract the matched question
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
                // Handle the error response (e.g., log or throw an exception)
                System.err.println("HTTP Request failed with response code: " + responseCode);
                System.out.println(requestBody);
                
            }

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that may occur during the API request
        }
        return responseList;
    }
        
}