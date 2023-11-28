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

    public List<DialogNode> getResponseNode(String userInput, String parentId) {
      
        List<DialogNode> matchingNodes = findMatchingNodeLocally(userInput, parentId);
        
      
        if (matchingNodes.isEmpty()) {
            matchingNodes = findMatchingNodeUsingOpenAI(userInput, parentId);
        }
        
    
        List<Response> responses = getResponsesForNode(matchingNodes);

       
        return buildResponse(matchingNodes, responses);
    }

    private List<DialogNode> findMatchingNodeLocally(String userInput, String parentId) {

        return dialogNodeRepository.findByParentIdAndDialogTextContainingIgnoreCase(parentId, userInput);
    }


    private List<DialogNode> findMatchingNodeUsingOpenAI(String userInput, String parentId) {
        
        List<String> potentialQuestions = dialogNodeRepository.findAllDialogTextByParentId(parentId);
        
        List<String> matchedQuestions = openAIService.getQuestionMatch(userInput, potentialQuestions);
        
        List<DialogNode> matchedNodes = matchedQuestions.stream()
            .map(qIndex -> potentialQuestions.get(Integer.parseInt(qIndex)))
            .map(qText -> dialogNodeRepository.findByDialogText(qText))
            .collect(Collectors.toList());
        return matchedNodes;
    }

    private List<Response> getResponsesForNode(List<DialogNode> nodes) {
       
        List<Response> responses = new ArrayList<>();
        for (DialogNode node : nodes) {
            List<DialogNodeToResponse> dnToResponses = dialogNodeResponseRepository.findByDialogNode(node);
            dnToResponses.forEach(r -> responses.add(r.getResponse()));
        }
        return responses;
    }

    private List<DialogNode> buildResponse(List<DialogNode> nodes, List<Response> responses) {
        
        return nodes; 
    }
}