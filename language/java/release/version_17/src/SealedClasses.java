/**
 * JEP 409: Sealed Classes
 * <a href="https://openjdk.org/jeps/409">Link</a>
 */
public class SealedClasses {
    private final static System.Logger log = System.getLogger("default");

    public static void main(String[] args) {
        final Grades firstGrade = new FirstGrade();
        final Grades secondGrade = new SecondGrade();
        final Grades thirdGrade = new ThirdGrade();
        final Grades forthGrade = new ForthGrade();
        
        log.log(System.Logger.Level.INFO, firstGrade.calculateVacationDays());
        log.log(System.Logger.Level.INFO, secondGrade.calculateVacationDays());
        log.log(System.Logger.Level.INFO, thirdGrade.calculateVacationDays());
        log.log(System.Logger.Level.INFO, forthGrade.calculateVacationDays());
    }
}

abstract sealed class Grades permits FirstGrade, SecondGrade, ThirdGrade {
    public static final int BASE_VACATION_DAYS_NUMBER = 15;
    
    abstract int calculateVacationDays();
}

non-sealed class FirstGrade extends Grades {
    @Override
    int calculateVacationDays() {
        return BASE_VACATION_DAYS_NUMBER;
    }
}
final class SecondGrade extends Grades {
    @Override
    int calculateVacationDays() {
        return BASE_VACATION_DAYS_NUMBER + 1;
    }
}
sealed class ThirdGrade extends Grades {
    @Override
    int calculateVacationDays() {
        return BASE_VACATION_DAYS_NUMBER + 3;
    }
}
final class ForthGrade extends ThirdGrade {
    @Override
    int calculateVacationDays() {
        return BASE_VACATION_DAYS_NUMBER + 5;
    }
}

class OutOfHierarchy extends FirstGrade {

}

/*
Cannot - SecondGrade is final
class Bom extends SecondGrade {}
 */

/*
Can't define because outside parents' namespace
final class UnwantedChild extends Grades {
    @Override
    int calculateVacationDays() {
        return 0;
    }
}
*/
