package com.thubot.core.repository;

import com.zegline.thubot.ThuBotApplication;
import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.repository.DialogNodeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ContextConfiguration(classes = ThuBotApplication.class)
public class DialogNodeRepositoryTests {

    @Autowired
    DialogNodeRepository dnr;
    @Test
    public void DialogNode_Find_ReturnFoundDialogNode(){

        //Arrange
        DialogNode testNode = DialogNode.builder()
                .id("QR1923")
                .dialogText("This is a test")
                .msgText("This is test message").build();

        //Act
        DialogNode savedDialogNode = dnr.save(testNode);

        //Assert
        Assertions.assertNotNull(savedDialogNode);
        Assertions.assertEquals(savedDialogNode, testNode);

    }

    @Test
    public void DialogNodeRelationship_Find_ReturnFoundRelationship(){

        //Arrange
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

        //Act
        DialogNode savedLeaf = dnr.save(leafNode);

        DialogNode savedRoot = dnr.save(rootNode);

        //Assert
        Assertions.assertNotNull(savedRoot.getChildren());
        //Assertions.assertEquals(dnr.findByChildren(savedLeaf), savedRoot);
    }

}