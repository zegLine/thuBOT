package com.zegline.thubot.core;


import com.zegline.thubot.ThuBotApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
@SpringBootTest(
        //webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ThuBotApplication.class)
class ApiApplicationTests {

    @Test
    void contextLoads() {
    }


}
