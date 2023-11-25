package com.posadskiy.java.release.v21.pattern;

import com.posadskiy.java.release.v21.pattern.records.*;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * JEP 440: Record Patterns
 * <a href="https://openjdk.org/jeps/440">Docs</a>
 * <p>
 * Enhance the Java programming language with record patterns to deconstruct record values. Record patterns and type 
 * patterns can be nested to enable a powerful, declarative, and composable form of data navigation and processing.
 */
@Log4j2
public class SystemRunner {

    public static void main(String[] args) {
        final SystemRunner systemRunner = new SystemRunner();
        var hotel = new Hotel(
            List.of(
                new Sensor(
                    new Hardware("IAQ Sensor", "1.0", 34))),
            new Location("Texas",
                new GeoPoint(115.2, 112.7)));
        systemRunner.processHotel(hotel);
        systemRunner.processHotelViaPattern(hotel);
    }

    private void processHotel(Hotel hotel) {
        log.info(hotel);
    }

    private void processHotelViaPattern(Hotel hotel) {
        if (hotel instanceof Hotel(List<Sensor> sensors, Location(String name, GeoPoint geoPoint))) {
            log.info(sensors);
            log.info(name);
            log.info(geoPoint);
        }
    }
}
