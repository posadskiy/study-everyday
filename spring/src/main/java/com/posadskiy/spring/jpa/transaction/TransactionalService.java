package com.posadskiy.spring.jpa.transaction;

import com.posadskiy.spring.jpa.model.Sensor;
import com.posadskiy.spring.jpa.repository.SensorRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class TransactionalService {

    private final SensorRepository repository;

    public TransactionalService(SensorRepository repository) {
        this.repository = repository;
    }

    public void doSomeTransactionalWork() {
        final List<Sensor> byTemperature = repository.findByTemperature(23.0);

        log.info("Size: " + byTemperature.size());
    }

    @Transactional(timeout = 0)
    public List<Sensor> transactionTimeout() {
        repository.save(new Sensor("Sensor", 23.0, 55.0));

        return repository.findByTemperature(23.0);
    }

    @Transactional(rollbackFor = {RuntimeException.class})
    public void writeSomeSensors() {
        repository.save(new Sensor("Sensor", 23.0, 55.0));
        repository.save(new Sensor("Wrong sensor", 124.45, 55.0));
    }
    
    public List<Sensor> getAll() {
        return IteratorUtils.toList(repository.findAll().iterator());
    }

}
