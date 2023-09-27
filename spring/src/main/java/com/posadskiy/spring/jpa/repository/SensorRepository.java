package com.posadskiy.spring.jpa.repository;

import com.posadskiy.spring.jpa.model.Sensor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SensorRepository extends CrudRepository<Sensor, Long> {
    List<Sensor> findByTemperature(Double temperature);
}
