package com.posadskiy.kata;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RaifTaskTest {

    @Test
    public void getTopAgeForName() {
        final RaifTask raifTask = new RaifTask();
        var persons = generatePersons();
        var size = 2;

        final List<Person> result = raifTask.getTopAgeForName(persons);

        assertEquals(size, result.size());
        assertEquals(25, result.get(0).age);
        assertEquals(11, result.get(1).age);
    }

    private List<Person> generatePersons() {
        return List.of(new Person("John", 23), new Person("John", 25), new Person("Mark", 11));
    }
}
