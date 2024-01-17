/**
 * @file DialogNodeMatchTests.java
 * @brief Test class for the DialogNodeMatch service
 *
 * This class contains unit tests for the DialogNodeMatch service. It tests the functionality 
 * of getting the correct response node based on the input and the interaction with the OpenAI service.
 */
package com.zegline.thubot.core.service.dialogNodeMatch;

import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.repository.DialogNodeRepository;
import com.zegline.thubot.core.repository.DialogNodeResponseRepository;
import com.zegline.thubot.core.service.openai.OpenAIService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class DialogNodeMatchTests {

    @Mock
    private DialogNodeRepository dialogNodeRepository;

    @Mock
    private DialogNodeResponseRepository dialogNodeResponseRepository;

    @Mock
    private OpenAIService openAIService;

    @InjectMocks
    private DialogNodeMatch dialogNodeMatch;

    private DialogNode rootNode; 

    /**
     * Setup method that runs before each test
     *
     * This method sets up the necessary components for each test
     */
    @BeforeEach
    public void setup() {
        rootNode = new DialogNode("RootNode", "RootResponse");
        when(dialogNodeRepository.findDialogNodesByParentIsNull()).thenReturn(Collections.singletonList(rootNode));      
    }

   /**
    * Test method for getResponseNode when there's no direct match but OpenAI provides a valid index
    *
    * This method tests a scenario where there's no direct match for the input, but the OpenAI service 
    * provides a valid index for a DialogNode match. It verifies that the right DialogNode is returned.
    */
    @Test
    public void testGetResponseNodeWithNoDirectMatchAndOpenAIProvidesValidIndex() {
        
        List<String> openAIResponses = Collections.singletonList("0");
        when(openAIService.getQuestionMatch(anyString(), anyList())).thenReturn(openAIResponses);
        DialogNode resultNode = dialogNodeMatch.getResponseNode("SomeInput");
        assertEquals(rootNode, resultNode, "The returned node should match the root node.");
    }

    /**
    * Test method for getResponseNode when OpenAI fails to provide a match
    *
    * This method tests a scenario where the OpenAI service fails to provide a valid match for the input. 
    * It verifies that a default DialogNode instance is returned when no match is found.
    */
    @Test
    public void testGetResponseNodeWithOpenAIFailure() {
        
        when(openAIService.getQuestionMatch(anyString(), anyList())).thenReturn(Collections.emptyList());
        DialogNode resultNode = dialogNodeMatch.getResponseNode("SomeInputWithOpenAIFailure");
        assertNotNull(resultNode, "A DialogNode instance should be returned even if OpenAI provides no match.");
        assertEquals("PROMPT GOES AGAINST OUR AULA", resultNode.getMsgText(), "The returned DialogNode should have the fallback msgText.");
    }
}