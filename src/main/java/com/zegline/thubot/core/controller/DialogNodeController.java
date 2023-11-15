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

@RestController
@RequestMapping("/api/dialognode")
public class DialogNodeController {

    @Autowired
    DialogNodeRepository dnr;

    @PostMapping("/createChild")
    public DialogNode dialog_node_create(@RequestBody Map<String, String> body) {
        String dialogNodeText = body.get("dialogNodeText");
        String msgText = body.get("msgText");
        String parentNodeId = body.get("parentNodeId");

        Optional<DialogNode> optionalParent = dnr.findById(parentNodeId);
        if (optionalParent.isPresent()) {
            DialogNode parent = optionalParent.get();
            DialogNode d = new DialogNode(dialogNodeText, msgText);
            dnr.save(d);
            parent.addChild(d);

            return parent;
        };

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "couldn't find parent"
        );
    }

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
            if (!match.isPresent()) {
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
