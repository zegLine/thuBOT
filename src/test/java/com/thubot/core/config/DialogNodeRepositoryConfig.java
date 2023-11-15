package com.thubot.core.config;

import com.zegline.thubot.core.repository.DialogNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@SpringBootConfiguration
public class DialogNodeRepositoryConfig {
    @Autowired
    static DialogNodeRepository dnr;
    @Bean
    public static DialogNodeRepository getRepo() {

        return dnr;
    }
}