package com.posadskiy.spring.jpa;

import com.posadskiy.spring.jpa.model.Sensor;
import com.posadskiy.spring.jpa.repository.SensorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaStarterTest {

    @Autowired
    private SensorRepository repository;

    @Test
    public void start() {
        repository.save(new Sensor(27.0, 50.0));
        repository.save(new Sensor(23.0, 45.0));
        repository.save(new Sensor(25.0, 35.0));
        repository.save(new Sensor(24.0, 65.0));
        repository.save(new Sensor(26.0, 55.0));

        final Iterable<Sensor> all = repository.findAll();
        for (Sensor sensor : all) {
            assertTrue(sensor.getTemperature() < 28 && sensor.getTemperature() > 22);
            assertTrue(sensor.getHumidity() < 66 && sensor.getHumidity() > 34);
        }
        
        repository.findByTemperature(23.0).forEach(sensor -> {
            assertEquals(45.0, sensor.getHumidity(), 0.00001);
        });
    }
}
