package com.posadskiy.spring.jpa.transaction;

import com.posadskiy.spring.jpa.model.Sensor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionTimedOutException;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TransactionalServiceTest {
    
    @Autowired
    private TransactionalService service;

    @Test
    public void doSomeTransactionalWork() {
        service.doSomeTransactionalWork();
    }
    
    @Test(expected = TransactionTimedOutException.class)
    public void transactionTimeout_runMethod_throwException() {
        service.transactionTimeout();
    }
    
    @Test
    public void writeSomeSensors_writeRecordsWithQueryError_noRecordsSavedInDb() {
        try {
            service.writeSomeSensors();
        } catch (RuntimeException ignored) {}
        final List<Sensor> all = service.getAll();
        assertEquals(0, all.size());
    }
}
