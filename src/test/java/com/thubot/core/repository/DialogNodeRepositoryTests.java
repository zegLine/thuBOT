package com.thubot.core.repository;

import com.thubot.core.config.DialogNodeRepositoryConfig;
import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.repository.DialogNodeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ContextConfiguration(classes = DialogNodeRepositoryConfig.class)
public class DialogNodeRepositoryTests {

    @Autowired
    DialogNodeRepository dnr = DialogNodeRepositoryConfig.getRepo();
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
        Assertions.assertEquals(savedDialogNode.getId(), testNode.getId());

    }
}
