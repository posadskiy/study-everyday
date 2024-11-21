package com.posadskiy.kata;

/**
 * table Person
 * - name 
 * - age
 *
 * select name, max(age) 
 * from Person
 * group by name
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * select p.name, p.age 
 * from Person as p
 * inner join
 * (
 * select name, age
 * from Person
 * group by name, age
 * having count(*) > 1
 * ) as pd
 * on p.name = pd.name and p.age = pd.age
 *
 * Input p
 * John 28
 * John 28
 * John 35
 *
 * Output pd
 * John 28
 *
 * Result
 * John 28
 * John 28
 *
 * John 28 John 28
 * John 28 John 28
 * John 35 null null
 * 
 * 
 * 3. Реализовать метод List<Person> getTopAgeForName(List<Person> persons), который повторяет логику из первой задачи
 */
public class RaifTask {
    List<Person> getTopAgeForName(List<Person> persons) {
        var map = new HashMap<String, Person>();
        persons.forEach(person -> {
            if (map.containsKey(person.name)) {
                if (person.age > map.get(person.name).age) {
                    map.put(person.name, person);
                }
            } else {
                map.put(person.name, person);
            }
        });
        
        return new ArrayList<>(map.values());
    }
    
    
}

class Person {
    String name;
    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
