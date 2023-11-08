package com.zegline.thubot.core.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/busisn")
    public Set<DialogNode> getAll() {
        Iterable<DialogNode> a = dnr.findAll();
        Set<DialogNode> cute = new HashSet<>() ;
        for (DialogNode dialogNode : a) {
            cute.add(dialogNode);
        }

        return cute;
    }
}
