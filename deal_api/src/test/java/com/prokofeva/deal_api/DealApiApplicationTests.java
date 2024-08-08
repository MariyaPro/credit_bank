package com.prokofeva.deal_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.main.lazy-initialization=true", classes = {DealApiApplicationTests.class})
class DealApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
