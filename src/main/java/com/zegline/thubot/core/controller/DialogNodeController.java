/**
 * @file DialogNodeController.java
 * @brief Controller for handling requests related to DialogNodes.
 * This controller provides REST endpoints for creating and retrieving DialogNodes
 * which are components of a conversational interface
 */
package com.zegline.thubot.core.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
 * @brief Provides REST endpoints to manage DialogNodes
 *
 * This controller class defines the routes for creating, modifying, deleting, and retrieving DialogNodes.
 * All endpoints of this controller are under the "/api/dialognode" route.
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

        // Validation is missing

        Optional<DialogNode> optionalParent = dnr.findById(parentNodeId);
        if (optionalParent.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "couldn't find parent"
            );
        };

        DialogNode parent = optionalParent.get();
        DialogNode d = DialogNode.builder().dialogText(dialogNodeText).msgText(msgText).build();
        dnr.save(d);
        parent.addChild(d);
        dnr.save(parent);
        return d;
    }

    /**
     * Modifies an existing DialogNode based on the provided data in the request body.
     *
     * @param body A map containing dialogNodeId and the new dialogNodeText, msgText, and parentNodeId.
     * @return The modified DialogNode.
     * @throws ResponseStatusException if the specified DialogNode is not found.
     */
    @PostMapping("/modify")
    public DialogNode dialog_node_modify(@RequestBody Map<String, String> body) {
        String id = body.get("dialogNodeId");
        String newDialogNodeText = body.get("dialogNodeText");
        String newMsgText = body.get("msgText");
        String newParentNodeId = body.get("parentNodeId");

        Optional<DialogNode> optionalNode = dnr.findById(id);
        if (optionalNode.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "couldn't find node"
            );
        }

        DialogNode node = optionalNode.get();
        node.setMsgText(newMsgText);
        node.setDialogText(newDialogNodeText);

        Optional<DialogNode> optionalNewParent = dnr.findById(newParentNodeId);
        if (optionalNewParent.isPresent()) {
            DialogNode newParent = optionalNewParent.get();

            node.setParent(newParent);
        }

        dnr.save(node);
        return node;
    }

    /**
     * Deletes a DialogNode based on the provided ID in the request body.
     *
     * @param body A map containing the ID of the DialogNode to delete.
     * @return The parent of the deleted DialogNode.
     * @throws ResponseStatusException if the specified DialogNode is not found,
     * if it has children, or the specified DialogNode is the root.
     */
    @PostMapping("/delete")
    public DialogNode dialog_node_delete(@RequestBody Map<String, String> body){
        String id = body.get("dialogNodeId");

        Optional<DialogNode> optionalNode = dnr.findById(id);
        if(optionalNode.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "couldn't find node"
            );
        }

        DialogNode node = optionalNode.get();

        DialogNode nodeParent = node.getParent();

        if (nodeParent == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Not allowed to delete root node"
            );
        }

        if (!node.getChildren().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "cannot delete nodes with children"
            );
        }

        nodeParent.getChildren().remove(node);
        dnr.delete(node);
        return nodeParent;

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
            List<DialogNode> nodes = dnr.findDialogNodesByParentIsNull();

            for (DialogNode node : nodes) {
                returned.add(node);
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
    //TODO CHANGE ROOT NODE TO QN0000 on josh db
    @GetMapping("/generateCSV")
    public ResponseEntity<byte[]> downloadCsv() throws IOException {
        List<String> FCK = new ArrayList<>();

        for (DialogNode d : dnr.findAll()){
            if (d.getParent() == null) {
                continue;
            }
            String ds = "" + d.getId().replace("QN","") + "," + d.getDialogText().replace("'","\'") + "," + d.getMsgText().replace("'","\'") + "," + d.getParent().getId().replace("QN","");
            FCK.add(ds);
        }

        // Generate CSV content
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
        for (String line : FCK) {
            writer.write(line);
            writer.write("\n");
        }
        writer.flush();

        // Set headers for the CSV file
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        headers.setContentDispositionFormData("attachment", "sample.csv");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(byteArrayOutputStream.toByteArray());
    }

}

