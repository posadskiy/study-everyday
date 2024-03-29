package com.posadskiy.spring.bpp;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class BppStarterTest {

    private final BppStarter runner;

    @Autowired
    BppStarterTest(BppStarter runner) {
        this.runner = runner;
    }

    @Test
    public void test() {
        runner.run();
    }
}
