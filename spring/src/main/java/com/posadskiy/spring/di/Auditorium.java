package com.posadskiy.spring.di;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class Auditorium {

    @PostConstruct
    public void turnOnLight() {
        System.out.println("Light is on");
    }

    @PreDestroy
    public void turnOffLight() {
        System.out.println("Light is off");
    }

}
