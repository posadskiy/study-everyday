package com.posadskiy.design.pattern.behavioural.chainofresponsibility;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Runner {

    public static void main(String[] args) {
        var rules = Rule.add(
            new OutOfRangeTemperatureRule(),
            new OutOfRangeHumidityRule()
        );

        var sensor = new Sensor(25.0, 102.0);

        log.info(
            String.valueOf(rules.orElseThrow(NoRuleException::new).check(sensor))
        );
    }
}
