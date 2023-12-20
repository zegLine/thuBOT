package com.thubot.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zegline.thubot.ThuBotApplication;
import com.zegline.thubot.core.controller.DialogNodeController;
import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.repository.DialogNodeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ThuBotApplication.class)
@WebMvcTest(DialogNodeController.class)

public class DialogNodeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //Mocks DialogNodeRepository
    @MockBean
    private DialogNodeRepository dialogNodeRepository;

    @Test
    public void testDialogNodeCreate() throws Exception {
        //input data
        String dialogNodeText = "ChildNode";
        String msgText = "Test";
        String parentNodeId = "parentID";

        //Mock parent
        DialogNode parent = DialogNode.builder()
                .id(parentNodeId)
                .dialogText("ParentNode")
                .build();
        when(dialogNodeRepository.findById(parentNodeId)).thenReturn(Optional.of(parent));

        //prepare request

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("dialogNodeText", dialogNodeText);
        requestBody.put("msgText", msgText);
        requestBody.put("parentNodeId", parentNodeId);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/createChild")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())  // Assuming your DialogNode has an "id" field
                .andExpect(jsonPath("$.dialogText").value(dialogNodeText))
                .andExpect(jsonPath("$.msgText").value(msgText));

        // Verify that the save method was called on the repository
        verify(dialogNodeRepository, times(2)).save(any(DialogNode.class));
    }
}
