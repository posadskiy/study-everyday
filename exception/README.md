# Exceptions
Исключения используются, когда выполнение программы зашло в тупик. Это происходит, когда невозможно продолжать выполнение
приложения в нормальном режиме.

#### Применимость
Одно из важных применений исключений - возможность с помощью одной команды убрать всё со стека. Перехват исключений 
используется, чтобы позволять очистку стека до определенного момента. Представим, что снизу представлен стек с уровнями
вложенности:

1 -\
2 --\
3 --- catch (MyException e) { ... }\
4 ----\
5 ----- throw new MyException();\

В данном примере, выбрасывая исключение на 5-ом "листе" стека, можно вернуться сразу на третий, минуя остальные и
продолжить выполнение программы.

##### Выброшенное дважды не поймать
Exception, выброшенные из блока catch, не перехватываются дальше, даже если соответствуют сигнатуре.
```java
public class DoubleThrow {
    public static void main(String[] args) {
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {
            throw e;
        } catch (Exception e) { // Здесь NPE не перехватится, хотя является наследником Exception
            System.out.println(e.getMessage());
        }
    }
}
```

#### Бросаем Exception внутри Throwable
Если выбрасывается родитель, внутри которого находится потомок, то в catch можно отловить потомка, но обязательно должен
быть обработчик на родителя или, как в случае ниже, сигнатура метода указывает возможность выброса родителя. Другие
варианты не пропустит компилятор.
```java
public class ThrowThrowableCatchException {
    public static void main(String[] args) throws Throwable {
        try {
            Throwable t = new Exception();
            throw t;
        } catch (Exception e) {
            System.err.println("Exception");
        }
    }
}
```

#### Наследование методов, бросающих исключения
Список исключений, которые могут быть выброшены из метода, при наследовании можно оставить прежним или сузить. Но
расширять нельзя. Это может привести к тому, что на месте использования родителя, будет получено неожиданное исключение,
"серьезнее" того, которые ожидаются и могут быть обработаны. В примере ниже в потомке описана возможность выброса
`IOException`, которое является родителем `FileNotFoundException`.
```java
class InheritanceMethodsWithThrow {
    protected void execute() throws FileNotFoundException {}
}

class Children extends InheritanceMethodsWithThrow {
    @Override
    protected void execute() throws IOException {}; 
}
```

#### System.err
`System.out` - буфферизованный поток вывода в консоль, а `System.err` - нет. Поэтому, в таком примере:
```java
public class Start {
    public static void main(String[] args) {
        System.out.println("Message");
        throw new Error("Error");
    }
}
```
может сначала вывестись `Message`, а затем `Error`, а может наоборот. Если же `System.out` заменить на `System.err`, то
всегда будет выведено сначала `Message`, потом `Error`.

#### Throw null
Java позволяет выбросить null-исключение: `throw null`. В этом случае происходит проверка аргумента и, в данном случае,
геренируется NullPointerException.