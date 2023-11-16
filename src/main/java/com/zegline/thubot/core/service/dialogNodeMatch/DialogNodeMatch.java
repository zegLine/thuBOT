/**
 * @file DialogNodeMatch.java
 * @brief Service Class for matching dialog nodes to user input
 *
 * This service class provides functionality to match user input with dialog nodes,
 * utilizing both local repository data and external OpenAI services
 */
package com.zegline.thubot.core.service.dialogNodeMatch;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @class DialogNodeMatch
 * @brief Service class for matching user input to dialog nodes
 *
 * This class offers methods to match user input against dialog nodes from a repository
 * or generate responses using OpenAI's services. It encapsulates the logic for the
 * dialog node matching process.
 */
@Service
public class DialogNodeMatch {
    
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
