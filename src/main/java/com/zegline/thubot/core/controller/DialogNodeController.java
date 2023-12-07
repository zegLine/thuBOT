/**
 * @file DialogNodeController.java
 * @brief Controller for handling requests related to DialogNodes.
 * This controller provides REST endpoints for creating and retrieving DialogNodes
 * which are components of a conversational interface
 */
package com.zegline.thubot.core.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.repository.DialogNodeRepository;


/**
 * @class DialogNodeController
 * @brief Controller class to manage DialogNode resources
 *
 * Provides the REST endpoints for creating and retrieving DialogNode entities. DialogNodes
 * are the constructs used to hold dialog data for open conversations according to sessionId
 */
@RestController
@RequestMapping("/api/dialognode")
public class DialogNodeController {

    @Autowired
    DialogNodeRepository dnr;


    /**
     * Creates a new DialogNode based on the provided data in the request body.
     *
     * @param body A map containing dialogNodeText and msgText to create the DialogNode.
     * @return The created DialogNode.
     */
    @PostMapping("/createChild")
    public DialogNode dialog_node_create(@RequestBody Map<String, String> body) {
        String dialogNodeText = body.get("dialogNodeText");
        String msgText = body.get("msgText");
        String parentNodeId = body.get("parentNodeId");

        Optional<DialogNode> optionalParent = dnr.findById(parentNodeId);
        if (optionalParent.isPresent()) {
            DialogNode parent = optionalParent.get();
            DialogNode d = DialogNode.builder().dialogText(dialogNodeText).msgText(msgText).build();
            dnr.save(d);
            parent.addChild(d);
            dnr.save(parent);
            return parent;
        };

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "couldn't find parent"
        );
    }

    /**
     * Retrieves DialogNode(s) based on the provided parameters or returns all DialogNodes if no parameters are specified.
     *
     * @param body A map containing parameters for filtering DialogNodes (optional).
     * @return Set of DialogNodes matching the provided criteria or all DialogNodes if no specific parameters are provided.
     * @throws ResponseStatusException If the provided ID is empty or if the DialogNode with the specified ID is not found.
     */

    @GetMapping("/get")
    public Set<DialogNode> get(@RequestBody (required = false) Map<String, String> body) {
        Set<DialogNode> returned = new HashSet<>() ;
        if(body == null){
            List<String> rootIds = dnr.findIdsWithNoChildren();

            for (String rootId : rootIds) {
                System.out.println(rootId);
                returned.add(dnr.findById(rootId).get());
            }

            return returned;
        }

        if(!body.get("id").isEmpty()) {
            Optional<DialogNode> match = dnr.findById(body.get("id"));
            if (match.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "couldn't find node"
                );
            }
            returned.add(match.get());

            return returned;
        }

        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "id cannot be empty"
        );
    }

}
