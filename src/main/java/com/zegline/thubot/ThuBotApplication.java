/**
 * @file ThuBotApplication.java
 * @brief Entry point for the ThuBot chatbot application
 *
 * This class serves as the main class. It initializes and configures the
 * Spring Boot application framework, which facilitates dependency injection,
 * embedded server configuration
 */
package com.zegline.thubot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @class ThuBotApplication
 * @brief Main application class for the ThuBot chatbot
 *
 * This is the main class that bootstraps the Spring Boot application. It contains the main method
 * which is the entry point of the Java application
 */
@SpringBootApplication
public class ThuBotApplication {

	 /**
     * The main method that starts up the Spring Boot application
     *
     * @param args An array of command-line arguments passed to the application
     */
	public static void main(String[] args) {
		SpringApplication.run(ThuBotApplication.class, args);
	}
}
