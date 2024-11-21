package com.posadskiy.spring.jpa.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(length = 10)
    private String name;
    
    @Min(value = -30, message = "Temperature is reached minimum")
    @Max(value = 55, message = "Temperature is reached maximum")
    private Double temperature;

    @Min(value = 0, message = "Humidity is reached minimum")
    @Max(value = 100, message = "Humidity is reached maximum")
    private Double humidity;

    public Sensor(String name, Double temperature, Double humidity) {
        this.name = name;
        this.temperature = temperature;
        this.humidity = humidity;
    }
}
