/**
 * @file DialogNodeTests.java
 * @brief Test class for the DialogNode entity
 *
 * This class contains unit tests for the DialogNode entity. It tests the functionality 
 * of creating a DialogNode object and adding child nodes to a parent node.
 */
package com.zegline.thubot.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @class DialogNodeTests
 * @brief Test class for the DialogNode
 *
 * This class contains unit tests for the DialogNode entity. It tests the functionality 
 * of creating a DialogNode object and adding child nodes to a parent node.
 */
@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class DialogNodeTests {

    private DialogNode dialognode;

    /**
     * Setup method that runs before each test
     *
     * This method sets up a DialogNode object to be used in each test
     */
    @BeforeEach
    public void setup() {
        dialognode = new DialogNode("Question", "Response");
    }

    /**
     * Test method for DialogNode creation
     *
     * This method tests the creation of a DialogNode object and its getters
     */
    @Test
    public void testDialogNodeCreation() {
        assertEquals("Question", dialognode.getDialogText());
        assertEquals("Response", dialognode.getMsgText());
    }

    /**
     * Test method for adding a child node to a DialogNode
     *
     * This method tests the method for adding a single child node to a DialogNode object
     */
    @Test
    public void testAddChild() {
        DialogNode childNode = new DialogNode("ChildQuestion", "ChildResponse");
        dialognode.addChild(childNode);

        assertTrue(dialognode.getChildren().contains(childNode));
        assertEquals(dialognode, childNode.getParent());
    }

    /**
     * Test method for adding multiple children to a DialogNode
     *
     * This method tests the method for adding multiple child nodes to a DialogNode object
     */
    @Test
    public void testAddChildren() {
        DialogNode child1 = new DialogNode("Child1Question", "Child1Response");
        DialogNode child2 = new DialogNode("Child2Question", "Child2Response");

        dialognode.addChildren(Set.of(child1, child2));

        assertTrue(dialognode.getChildren().contains(child1));
        assertTrue(dialognode.getChildren().contains(child2));
        assertEquals(dialognode, child1.getParent());
        assertEquals(dialognode, child2.getParent());
    }
}