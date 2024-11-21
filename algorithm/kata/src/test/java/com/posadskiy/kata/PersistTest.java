package com.posadskiy.kata;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import static org.testng.Assert.*;

@Log4j2
public class PersistTest {

    @Test
    public void BasicTests() {
        log.info("****** Basic Tests ******");
        assertEquals(3, Persist.persistence(39));
        assertEquals(0, Persist.persistence(4));
        assertEquals(2, Persist.persistence(25));
        assertEquals(4, Persist.persistence(999));
    }

}
