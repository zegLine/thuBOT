package com.zegline.thubot.core;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Autowired;

import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.repository.DialogNodeRepository;

@Component
@Profile("!test") 
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DialogNodeRepository dialogNodeRepository;

    @Override
    public void run(String... args) throws Exception {
       
        dialogNodeRepository.deleteAll();

        DialogNode rootNode = new DialogNode();
        rootNode.setDialogText("What can I help you with?");
        rootNode.setMsgText("Here are some options you might be interested in.");
        rootNode = dialogNodeRepository.save(rootNode);

        DialogNode childNode1 = new DialogNode();
        childNode1.setDialogText("I have a question about my account.");
        childNode1.setMsgText("Sure, please specify your question about the account.");
        childNode1.setParent(rootNode);
        dialogNodeRepository.save(childNode1);

        DialogNode childNode2 = new DialogNode();
        childNode2.setDialogText("I need support for my device.");
        childNode2.setMsgText("Certainly! What issues are you experiencing with your device?");
        childNode2.setParent(rootNode);
        dialogNodeRepository.save(childNode2);

        rootNode.getChildren().add(childNode1);
        rootNode.getChildren().add(childNode2);
        dialogNodeRepository.save(rootNode);
    }
}