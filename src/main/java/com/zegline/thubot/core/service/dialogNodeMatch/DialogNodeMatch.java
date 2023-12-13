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

import java.util.*;
import java.util.stream.Collectors;

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
     * @return The fetched answer or a default message if no suitable response is found.
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
            return new DialogNode();

        String unsafeNum = responseList.get(0).replace("QUESTION", "");
        unsafeNum = unsafeNum.replace("\"", "");

        try {
            int num = Integer.parseInt(unsafeNum);
            return possibleNodes.get(num);
        } catch (Exception e) {
            return DialogNode.builder().msgText("PROMPT GOES AGAINST OUR AULA").build();
        }


    }

    private List<String> getAnswers(List<DialogNode> nodes) {
        // Implement your logic to extract answers from the list of DialogNodes
        // You can loop through the nodes and extract the msgText or other relevant data
        List<String> answers = new ArrayList<>();
        for (DialogNode node : nodes) {
            answers.add(node.getMsgText());
        }
        return answers;
    }

    public static List<DialogNode> getNodesRecursively(DialogNode root, int recurseLevel) {
        List<DialogNode> result = new ArrayList<>();
        getNodesRecursivelyHelper(root, recurseLevel, result);
        return result;
    }

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
     * @param input The String to match
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

        System.out.println(words);
        return null;
    }

        
}