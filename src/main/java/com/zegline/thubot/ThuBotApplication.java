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
     * @param args A string array containing command-line arguments that were passed to this application.
     * The Spring Boot SpringApplication.run() method is called inside the main method, which initializes the framework and
     * starts up the embedded web server (provided by Spring Boot) allowing the application to serve HTTP requests.
     */
	public static void main(String[] args) {
		SpringApplication.run(ThuBotApplication.class, args);
	}
}
