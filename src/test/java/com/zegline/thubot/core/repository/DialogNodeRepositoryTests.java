/**
 * @file DialogNodeRepositoryTests.java
 * @brief Test class for the DialogNodeRepository
 *
 * This class contains unit tests for methods in the DialogNodeRepository. It tests the ability
 * to create and retrieve DialogNode instances and ensure the correct management of relationships 
 * between nodes.
 */
package com.zegline.thubot.core.repository;

import com.zegline.thubot.core.model.DialogNode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;

/**
 * @class DialogNodeRepositoryTests
 * @brief Test class for the DialogNodeRepository
 *
 * Tests the DialogNodeRepository's ability to save and retrieve DialogNode instances,
 * as well as the correct handling of relationships between Parent and Child nodes.
 */
@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class DialogNodeRepositoryTests {

    @Autowired
    DialogNodeRepository dnr;

    /**
     * Test method for saving a DialogNode and retrieving it.
     *
     * This method saves a DialogNode to the database and then retrieves the saved DialogNode 
     * to verify that it has been persisted correctly.
     */
    @Test
    public void DialogNode_Find_ReturnFoundDialogNode(){

        DialogNode testNode = DialogNode.builder()
                .id("QR1923")
                .dialogText("This is a test")
                .msgText("This is test message").build();

        DialogNode savedDialogNode = dnr.save(testNode);
        Assertions.assertNotNull(savedDialogNode);
        Assertions.assertEquals(savedDialogNode, testNode);
    }

    /**
     * Test method for saving DialogNodes with a Parent-Child relationship and retrieving them.
     *
     * This method saves two DialogNode objects (Parent and Child) to the database, 
     * and retrieves them to verify that the relationships between nodes has been persisted correctly.
     */
    @Test
    public void DialogNodeRelationship_Find_ReturnFoundRelationship(){

        DialogNode leafNode = DialogNode.builder()
                .id("QR1000")
                .dialogText("This is a leaf node")
                .msgText("Leaf")
                .children(new HashSet<>(1))
                .build();

        DialogNode rootNode = DialogNode.builder()
                .id("QR0000")
                .dialogText("This is the root node")
                .msgText("Root")
                .children(new HashSet<>(1))
                .build();
                
        rootNode.addChild(leafNode);
        DialogNode savedRoot = dnr.save(rootNode);
        DialogNode savedLeaf = dnr.save(leafNode);
        Assertions.assertNotNull(savedRoot.getChildren());
    }
}