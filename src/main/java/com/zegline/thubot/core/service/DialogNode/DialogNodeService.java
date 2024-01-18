package com.zegline.thubot.core.service.DialogNode;

import com.zegline.thubot.core.model.DialogNode;
import com.zegline.thubot.core.repository.DialogNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DialogNodeService {

    @Autowired
    private DialogNodeRepository dialogNodeRepository;

    public void checkForRootNode() {
        // Check for the existence of a root node on startup
        List<DialogNode> rootList = dialogNodeRepository.findDialogNodesByParentIsNull();

        if (rootList == null || rootList.isEmpty()) {
            throw new IllegalStateException("Root node does not exist or is inaccessible");
        }

        if (rootList.size() > 1) {
            throw new IllegalStateException("There is more than one root node in the database");
        }

    }

    public DialogNode getRootNode() {
        // Get the root node by querying for nodes with null parent
        List<DialogNode> rootList = dialogNodeRepository.findDialogNodesByParentIsNull();

        // We should NOT end up here, if the application checks for a root node when it boots up
        if (rootList == null || rootList.isEmpty()) {
            throw new IllegalStateException("Root node does not exist or is inaccessible");
        }

        return rootList.get(0);
    }

}
