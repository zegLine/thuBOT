package com.zegline.thubot.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class DialogNodeTests {

    private DialogNode dialognode;

    @BeforeEach
    public void setup() {
        dialognode = new DialogNode("Question", "Response");
    }

    @Test
    public void testDialogNodeCreation() {
        assertEquals("Question", dialognode.getDialogText());
        assertEquals("Response", dialognode.getMsgText());
    }

    @Test
    public void testAddChild() {
        DialogNode childNode = new DialogNode("ChildQuestion", "ChildResponse");
        dialognode.addChild(childNode);

        assertTrue(dialognode.getChildren().contains(childNode));
        assertEquals(dialognode, childNode.getParent());
    }

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
