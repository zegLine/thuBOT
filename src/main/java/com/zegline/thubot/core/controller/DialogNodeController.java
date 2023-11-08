package com.zegline.thubot.core.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    @PostMapping("/create")
    public DialogNode dialog_node_create(@RequestBody Map<String, String> body) {
        String dialogNodeText = body.get("dialogNodeText");
        String msgText = body.get("msgText");

        DialogNode d = new DialogNode(dialogNodeText, msgText);
        dnr.save(d);
        return d;
    }

    @GetMapping("/get")
    public Set<DialogNode> get(@RequestBody (required = false) Map<String, String> body) {
        Set<DialogNode> returned = new HashSet<>() ;
        Iterable<DialogNode> a;
        if(body == null){
            a = dnr.findAll();
            
            for (DialogNode dialogNode : a) {
                returned.add(dialogNode);
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
