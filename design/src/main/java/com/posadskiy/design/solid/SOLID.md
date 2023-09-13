### SOLID

**SOLID** is set of software design principles:

S - single-responsibilities principle\
O - Open-close principle\
L - Liskov substitution principle\
I - Interface segregation principle\
D - Dependency inversion principle

#### Single-responsibility principle

Class (component) should be responsible for one action only. In the other words, should be only one reason for changing
the class. For example, class responsible for calculating average building consumption shouldn't implement the ways for
presenting the result to user.

Example:

```java
class SiteAverageConsumptionCalculator {
    public Consumption calculateInKWh() {
        // calculations
    }
}

class ConsumptionJsonPresenter {
    public String toJson(Consumption consumption) {
        // some wrapping
    }
}
```

But not:

```java
class SiteAverageConsumptionCalculator {

    public String calculateInKWh() {
        // calculations
        // then mapping to JSON
    }
}
```

If do not follow,

- code becomes less readable,
- many side effects could be expected from method call,
- changes in class in one logical place could break code in the other logical place.

#### Open-close principle

Class (component) should be open for extension, but close for modification.

Base class should stay as if, and, if new functionality is going to be implemented, it should be included into child 
(extender) classes.

Example:

```java
class ConsumptionCalculator {
    public Consumption calculate(Double kWh, Integer hours) {
        return new Consumption(kWh * hours);
    }
}

class ConsumptionOccupancyRateCalculator() extends ConsumptionCalculator {
    @Override
    public Consumption calculate(Double kWh, Integer hours) {
        return new Consumption(kWh * hours * OccupancyRateService.getOccupancyRate());
    }
}
```

But not 

```java
class ConsumptionCalculator {
    public Consumption calculate(Double kWh, Integer hours, Double occupancyRate) {
        return new Consumption(kWh * hours * occupancyRate);
    }
}
```

If do not follow,
- API could be broken
- Needs to fix unit/integration tests
- Needs to fix broken dependencies in the code
- Bugs farm


#### Liskov substitution principle

Class (component) should be replaceable with its own subclass. 
Simple example - initialize instance of ArrayList, but place in variable of List type.


Example:
```java
class ConsumptionCalculator {
    public Consumption calculate(Double kWh, Integer hours) {
        // return Consumption instance
    }
}

class ConsumptionOccupancyRateCalculator extends ConsumptionCalculator {
    
    @Override
    // the same parameters as in parent class
    public Consumption calculate(Double kWh, Integer hours) {
        // calculation could be different, but
        // also return Consumption instance
    }
}
```

If do not follow:
- possible exceptions in code
- the real problem to rely on code and create any expectations


#### Interface segregation principle

Implementer of interface shouldn't be forced to implement or depend on unneeded methods.
If class implements interface 

For example,
```java
interface Occupable {
    double occupancyRate();
}

class FiveFloorsEuropeanHotel implements Occupable {
    @Override
    public double occupancyRate() {
        return 0.5; // should be dynamic
    }
}
```

But not
```java
interface Occupable {
    double occupancyRate();
    void refreshRate();
}

class FiveFloorsEuropeanHotel implements Occupable {
    @Override
    public double occupancyRate() {
        return 0.5; // should be dynamic
    }
    
    @Override
    public void refreshRate() {
        // common, I shouldn't do any refreshes...
    }
}
```

#### Dependency inversion principle

Class (component) should depend on abstractions, not concrete implementation.
Also, high-level modules shouldn't depend on low-level modules.

Example:
```java
class ConsumptionPrinter {
    public static void printConsumptionToLog(Consumption consumption) {
        // some nice work
    }
}

class App {
    public void start() {
        // some work
        // two lines below accept instances of two different models, but both based on parent Consumption
        ConsumptionPrinter.printConsumptionToLog(pureConsumption);
        ConsumptionPrinter.printConsumptionToLog(basedOnOccupancyRateConsumption);
        // more work
    }
}
```

but not
```java
class ConsumptionPrinter {
    public static void printConsumptionToLog(BasedOnOccupancyRateConsumption consumption) {
        // some nice work
    }
}
```
