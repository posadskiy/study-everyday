package com.posadskiy.spring.di;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class Auditorium {

    @PostConstruct
    public void turnOnLight() {
        log.info("Light is on");
    }

    @PreDestroy
    public void turnOffLight() {
        log.info("Light is off");
    }

}
