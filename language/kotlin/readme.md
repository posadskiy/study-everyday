### Start application

Main function - *fun main()*. Use parameters for this function when it necessary.


### Variable

* val (value) - **read-only** variables
* var (variable) - **mutable** variables


### String


There are String templates:
```kotlin
val name = "Your name"
"Hello, $name"
```

### Method

Call methods with parameter names.
```kotlin
fun name(first: Int, second: Int, third: Int) = first + second + third

name(third = 3, second = 2, first = 1)
name(second = 2, third = 3, first = 1)
```

Default values
```kotlin
fun name(age: Int = 31, name: String = "Robby") = println(age + name)

name() // 31Robby
name(20) // 20Robby

```