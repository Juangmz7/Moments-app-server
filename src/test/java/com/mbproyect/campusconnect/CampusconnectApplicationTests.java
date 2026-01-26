package com.mbproyect.campusconnect;

import com.mbproyect.campusconnect.config.TestContainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfig.class)
class CampusconnectApplicationTests {

    @Test
    void contextLoads() {
    }

}
