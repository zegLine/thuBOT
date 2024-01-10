/**
 * @file ApiApplicationTests.java
 * @brief Test class for the main application context
 *
 * This class contains unit tests for the main application context of the ThuBot application.
 * It tests the load and initialization of the application context.
 */
package com.zegline.thubot.core;

import com.zegline.thubot.ThuBotApplication;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * @class ApiApplicationTests
 * @brief Test class for the main application context
 *
 * This class contains a test to load and initialize the application context.
 */
@SpringBootTest(classes = ThuBotApplication.class)
class ApiApplicationTests {

    /**
     * Tests the application context loading.
     *
     * This test case ensures that the application context loads successfully without any issues.
     */
    @Test
    void contextLoads() {
    }
}
