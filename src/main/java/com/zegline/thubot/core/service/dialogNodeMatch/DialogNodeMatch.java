/**
 * @file DialogNodeMatch.java
 * @brief Service Class for matching dialog nodes to user input
 *
 * This service class provides functionality to match user input with dialog nodes,
 * utilizing both local repository data and external OpenAI services
 */
package com.zegline.thubot.core.service.dialogNodeMatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.model.DialogNodeToResponse;
import com.zegline.thubot.core.model.Response;
import com.zegline.thubot.core.repository.DialogNodeRepository;
import com.zegline.thubot.core.repository.DialogNodeResponseRepository;
import com.zegline.thubot.core.service.openai.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;

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
    private DialogNodeRepository dialogNodeRepository;

    @Autowired
    private DialogNodeResponseRepository dialogNodeResponseRepository;

    @Autowired
    private OpenAIService openAIService;
    
    /**
     * Matches user input with responses in the databases.
     *
     * @param userInput The input string the user provided, could be the prompt or natural language.
     * @param parentId  The id of the parent node.
     * @return The fetched answer or a default message if no suitable response is found.
     */
    public String getResponseNode(String userInput, String parent_id){

        //First match with one node in the database, and if not found, send the user input and the
        // subtree and the current parent text to openAI
        String machedNode = matchNodeToInput(parent_id);
        List<String> responseList = new ArrayList<>();
        List<String> possibleResponses = new ArrayList<>();


<<<<<<< HEAD
        if(machedNode.equals("null")){
=======
        if(machedNode == null){
>>>>>>> origin/main
            // Get Leaf Nodes that are Descendants of Parent
            possibleResponses = dialogNodeRepository.findLeafNodesByParentIdAsDescendants(parent_id);

            if(possibleResponses.isEmpty()) {
                possibleResponses = dialogNodeRepository.findLeafNodesExceptDescendantsOfParentId(parent_id);
                // If still Empty
                if(possibleResponses.isEmpty())
                    possibleResponses = dialogNodeRepository.findIdsWithNoChildren();
            }
            responseList = openAIService.getQuestionMatch(userInput, getAnswers(possibleResponses));

            // If no response and we didn't already send all Leaf Nodes to OpenAI
            if(responseList.isEmpty() && possibleResponses.equals(dialogNodeRepository.findIdsWithNoChildren())) {
                possibleResponses = dialogNodeRepository.findLeafNodesExceptDescendantsOfParentId(parent_id);
                if (possibleResponses.isEmpty())
                    possibleResponses = dialogNodeRepository.findIdsWithNoChildren();
                responseList = openAIService.getQuestionMatch(userInput, getAnswers(possibleResponses));
            }
        }else{
            //Todo return DataBase match

        }

        if(responseList.isEmpty())
            return "Sorry I could not find an answer for this";
        else{
           String num = responseList.get(0).replace("QUESTION", "");
           num = num.replace("\"", "");
            return dialogNodeRepository.findMSGTextById(possibleResponses.get(Integer.parseInt(num)));
        }

    }

    /**
     * Fetches the dialog texts for the provided list of node IDs.
     *
     * @param ids List of DialogNode IDs.
     * @return List of dialog texts corresponding to the given IDs.
     */
    private List<String> getAnswers(List<String> ids) {
        List<String> answers = new ArrayList<>();
        for (String id : ids) {
            answers.add(dialogNodeRepository.findDialogTextById(id));
        }
        return answers;
    }

    /**
     * Placeholder method to implement database matching logic.
     *
     * @param parentId ID of the Parent node.
     * @return Matched node ID, if found, otherwise null.
     */
    private String matchNodeToInput(String parentId) {
        // TODO: Implement database matching logic
        return null;
    }

        
}