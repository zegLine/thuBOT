/**
 * @file DialogNodeMatch.java
 * @brief Service Class for matching dialog nodes to user input
 *
 * This service class provides functionality to match user input with dialog nodes,
 * utilizing both local repository data and external OpenAI services
 */
package com.zegline.thubot.core.service.dialogNodeMatch;

import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.repository.DialogNodeRepository;
import com.zegline.thubot.core.service.openai.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private DialogNodeRepository dnr;


    /**
     * Matches the user input with responses in the databases
     * @param userInput <b>String</b> The input string the user provided, could be the prompt or natural langauge.
     * @param parent_id <b>String</b> The id of the parent node.
     * @return <b>String</b> of the fetched answer, "null" if doesn't exist.
     * 
     */
    public String getResponseNode(String userInput, String parent_id){
        
        //First match with one node in the database, and if not found, send the user input and the 
        // subtree and the current parent text to openAI
        String machedNode = matchNodeToInput(parent_id);
        List<String> responseList = new ArrayList<>();


        if(machedNode.equals("null")){
            // Get Leaf Nodes that are Descendants of Parent
            List<String> possibleResponses = dnr.findLeafNodesByParentIdAsDescendants(parent_id);

            if(possibleResponses.isEmpty()) {
                possibleResponses = dnr.findLeafNodesExceptDescendantsOfParentId(parent_id);
                // If still Empty
                if(possibleResponses.isEmpty())
                    possibleResponses = dnr.findIdsWithNoChildren();
            }
            responseList = OpenAIService.getQuestionMatch(userInput, possibleResponses);

            // If no response and we didn't already send all Leaf Nodes to OpenAI
            if(responseList.isEmpty() && possibleResponses.equals(dnr.findIdsWithNoChildren())) {
                possibleResponses = dnr.findLeafNodesExceptDescendantsOfParentId(parent_id);
                if (possibleResponses.isEmpty())
                    possibleResponses = dnr.findIdsWithNoChildren();
                responseList = OpenAIService.getQuestionMatch(userInput, possibleResponses);
            }
        }else{
            //Todo return DataBase match

        }

        if(responseList.isEmpty())
            return "Sorry I could not find an answer for this";
        else
            return responseList.get(0);
    }

    /**
     *
     * @param parent_id <b>String</b> ID of the Parent node, to go down this Prompt Tree
     */

    private String matchNodeToInput(String parent_id){
        // Todo Implement Database Matching
        return "null";
    }

        
}
