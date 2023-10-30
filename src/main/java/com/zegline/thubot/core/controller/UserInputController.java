package com.zegline.thubot.core.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/input")
public class UserInputController {

    @Value("${openai.api.key}") // Read the API key from your application.properties or application.yml file
    private String openaiApiKey;

    @GetMapping("/ask")
    public String input_ask(@RequestBody() Map<String, String> body) {
        String question = body.get("question");

        return getQuestionMatch(question);
    }

    public String getQuestionMatch(String input_question) {
        List<String> available_questions = new ArrayList<>();
        available_questions.add("Where is the closest campus?");
        available_questions.add("How do i register for a course?");
        available_questions.add("When is the admission deadline?");
        available_questions.add("What course programs does the THU offer?");

       // Prepare the input for the OpenAI API
        StringBuilder openaiInput = new StringBuilder("You will match the user input to one of the following questions:");
        for (String question : available_questions) {
            openaiInput.append("QUESTION").append(available_questions.indexOf(question)).append(":").append(question).append(";");
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
                    return root.get("choices").get(0).get("message").get("content").toString();
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
        return "";
    }
}
