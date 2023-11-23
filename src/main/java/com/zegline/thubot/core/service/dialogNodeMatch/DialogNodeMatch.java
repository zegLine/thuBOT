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
        List<String> responseList;
        List<String> possibleResponses;

        if(machedNode.equals("null")){
            Optional<DialogNode> nodes = dnr.findById(parent_id);
            if(nodes.isEmpty())
                return "null";
            for (int i = 0; i < nodes.stream().count(); i++){

            }
            //responseList = OpenAIService.getQuestionMatch(userInput, possibleResponses);

        }else{
            //Todo return Node content

        }


        //if(responseList.size()!=0){
        //    return responseList.get(0);
       //}

        return "null";
    }

    /**
     *
     * @param parent_id <b>String</b> ID of the Parent node, to go down this Prompt Tree
     */

    private String matchNodeToInput(String parent_id){

        return "null";
    }

    /**
     * Get the Nodes with the Answers on the Bottom of the Promt Tree
     * @param parent_id
     * @return
     */
    private Set<DialogNode> getAnswersOfCurrentPromtTree(String parent_id){
        Optional<DialogNode> node = dnr.findById(parent_id);
        Set<DialogNode> answers = new HashSet<>();
        if (node.isPresent()){
            Set<DialogNode> children = node.get().getChildren();

        }

        return answers;
    }

        
}
