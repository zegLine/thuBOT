/**
 * @file DialogNodeMatch.java
 * @brief Service Class for matching dialog nodes to user input
 *
 * This service class provides functionality to match user input with dialog nodes,
 * utilizing both local repository data and external OpenAI services.
 */
package com.zegline.thubot.core.service.dialogNodeMatch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.repository.DialogNodeRepository;
import com.zegline.thubot.core.repository.DialogNodeResponseRepository;
import com.zegline.thubot.core.service.openai.OpenAIService;

import java.util.*;

/**
 * @class DialogNodeMatch
 * @brief Service class for matching user input to dialog nodes
 *
 * This class offers methods to match user input against dialog nodes from a repository
 * or generate responses using OpenAI's services. It encapsulates the logic for the
 * dialog node matching process.
 */

@Setter
@Getter
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
    * @param userInput User input string, could be the prompt or natural language.
    * @return The fetched answer or a default message if no suitable response is found.
    *
    * @throws Exception If there's an error parsing the matched question number returned by OpenAI.
    */
    public DialogNode getResponseNode(String userInput){

        int recurseLevel = 15;

        // Get the root node
        DialogNode root = dialogNodeRepository.findDialogNodesByParentIsNull().get(0);

        // First match with one node in the database
        // if it is found, return it immediately
        DialogNode matchedNode = matchNodeToInput(userInput);
        if (matchedNode != null) return matchedNode;

        // Not directly found, therefore call OpenAI service
        List<DialogNode> possibleNodes = getNodesRecursively(root, recurseLevel);
        List<String> possibleResponses = getAnswers(possibleNodes);
        List<String> responseList = openAIService.getQuestionMatch(userInput, possibleResponses);

        if(responseList.isEmpty())
            return DialogNode.builder().msgText("PROMPT GOES AGAINST OUR AULA").build();

        String unsafeNum = responseList.get(0).replace("QUESTION", "");
        unsafeNum = unsafeNum.replace("\"", "");

        try {
            int num = Integer.parseInt(unsafeNum);
            return possibleNodes.get(num);
        } catch (Exception e) {
            return DialogNode.builder().msgText("PROMPT GOES AGAINST OUR AULA").build();
        }


    }

    /**
    * This method retrieves all answers from a list of dialog nodes.
    *
    * @param nodes A list of dialog nodes from which to extract responses
    * @return A list of answers, represented by the values of the msgText fields of the dialog nodes.
    */
    private List<String> getAnswers(List<DialogNode> nodes) {
        // Implement your logic to extract answers from the list of DialogNodes
        // You can loop through the nodes and extract the msgText or other relevant data
        List<String> answers = new ArrayList<>();
        for (DialogNode node : nodes) {
            answers.add(node.getMsgText());
        }
        return answers;
    }

    /**
    * This method recursively retrieves all dialog nodes starting from a root node.
    *
    * @param root The root dialog node from which to start the search.
    * @param recurseLevel The maximum depth of the search.
    * @return A list of all dialog nodes reachable from the root node, up to the specified search depth.
    */
    public static List<DialogNode> getNodesRecursively(DialogNode root, int recurseLevel) {
        List<DialogNode> result = new ArrayList<>();
        getNodesRecursivelyHelper(root, recurseLevel, result);
        return result;
    }

    /**
    * This method is a helper for the getNodesRecursively method, executing the actual recursion.
    *
    * @param node The node from which to start the search.
    * @param recurseLevel The remaining search depth.
    * @param result A running list of dialog nodes found so far.
    */
    private static void getNodesRecursivelyHelper(DialogNode node, int recurseLevel, List<DialogNode> result) {
        if (node == null || recurseLevel < 0) {
            return;
        }

        result.add(node);

        // Eagerly load children
        node.getChildren().size();

        if (recurseLevel > 0) {
            for (DialogNode child : node.getChildren()) {
                getNodesRecursivelyHelper(child, recurseLevel - 1, result);
            }
        }
    }

    /**
     * Placeholder method to implement database matching logic.
     *
     * @param input input string to match
     * @return Matched node if found, otherwise null.
     */
    private DialogNode matchNodeToInput(String input) {
        // TODO: Implement database matching logic
        input = input.toLowerCase();
        Set<String> stopWords = new HashSet<>(
                Arrays.asList(
                        "a", "about", "all", "and", "any", "at", "be",
                        "because", "been", "before", "being", "between", "by", "can",
                        "come", "could", "did", "do", "each", "for", "from", "he",
                        "have", "having", "her", "him", "his", "how", "i","if", "in",
                        "is", "it", "its", "just", "me", "more", "most", "my", "of",
                        "on", "one", "only", "or", "our", "over", "same", "still",
                        "see", "should", "so", "some", "such", "that", "the",
                        "their", "them", "there", "these", "they", "this", "to", "too",
                        "two", "was", "were", "what", "when", "where", "which", "who",
                        "with", "would", "you", "your")
        );
        StringTokenizer tokenizer = new StringTokenizer(input);

        List<String> words = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            words.add(tokenizer.nextToken());
        }
        words.removeAll(stopWords);
        List<DialogNode> possibleNodes = getNodesRecursively(dialogNodeRepository.findDialogNodesByParentIsNull().get(0), 15);
        DialogNode response = new DialogNode();
        int dialogMatches = 0;

        for(DialogNode node: possibleNodes) {
            String question = node.getDialogText();
            int matches = 0;
            for (String word : words) {
                boolean containsWord = question.contains(word);
                boolean containsPartOfWord = question.matches(".*\\b" + word + "\\b.*");
                if ((containsWord || containsPartOfWord))
                    matches++;
            }
            if (matches > dialogMatches) {
                dialogMatches = matches;
                response = node;
            }
        }
        if(dialogMatches >= 3){
            System.out.println("Dialog Node Matches: "+ response.getId());
            return response;
        }
        return null;
    }

        
}