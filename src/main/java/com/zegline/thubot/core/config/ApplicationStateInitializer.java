package com.zegline.thubot.core.config;

import com.zegline.thubot.core.service.DialogNode.DialogNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Profile("@!test")
public class ApplicationStateInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DialogNodeService dialogNodeService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Check if there is a root node
        dialogNodeService.checkForRootNode();
    }
}