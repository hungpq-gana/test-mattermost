package com.edso.testmatermost;

import com.edso.testmatermost.model.User;
import com.edso.testmatermost.repository.UserClient;
import com.edso.testmatermost.worker.MessageReceiver;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestMatermostApplicationTests {

    @Test
    void contextLoads() {
        UserClient userClient = new UserClient();
        User user = new User("hung17598@gmail.com","17598Hung!","gk8s344erjdjxkyoedqk13qhca");
    }
}
