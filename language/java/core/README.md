#### Initialization

Ways to initialize a class field, in order of execution:

- Initialization in the superclass
- Initialization at declaration time  
  `String name = "John"`
- Static initialization block  
  `static { name = "Garry" }`
- Instance initialization block  
  `{ this.name = "Mary" }`
- Constructor  
  `public Person() { this.name = "Alex" }`

Instance and class variables are initialized with default values; local variables are not.

A class may contain a method with the same name as the class. It must return a value or be `void`.

Static initializers are typically used to assign values to static fields when more than one line is needed (for example,
initializing an `ArrayList` with values). In that case it’s common to keep all static initialization in one place for
readability.

A default constructor is generated if the class contains no constructors.

Final variables can be initialized exactly once — either at declaration or in an initialization block.

#### Primitive types

| Type | Size | Default value |
| --- | --- | --- |
| byte | 8 bits | 0 |
| short | 16 bits | 0 |
| integer | 32 bits | 0 |
| long | 64 bits | 0 |
| float | 32 bits | 0.0 |
| double | 64 bits | 0.0 |
| char | 16 bits | \\u0000 |

You can write `int` and `double` literals with `_` as a digit separator:

`int value = 1_000_000`  
`double balance = 2_232.0_0`

#### Reference types

A method can return `null` if it returns an object type. You can’t return `null` from a `void` method.

#### Imports

If classes are in the same package, you don’t need an import for one to use the other.

In a class file, the only strictly required element is the class declaration itself.
The `package` line can be absent if the class is in the default package.
The import list may also be absent.

#### Running from the console

`java MyClass parameterOne "parameterTwo"`

#### Comparison

You can’t compare different data types using `==`.

#### Collections

##### ArrayList

Calling `equals` on two lists with the same elements returns `true`.

##### Varargs

If you pass `null` instead of values, it will cause an NPE.

#### Conversions

At a method call site, Java can perform a single implicit conversion (e.g., `int -> Integer` or `int -> long`). But it
won’t allow two-step conversions like `int -> long -> Long`, as in the example below:

```java
public class TooManyConversions {
    public static void play(Long l) { }
    public static void play(Long... l) { }
    public static void main(String[] args) {
        play(4); // DOES NOT COMPILE
        play(4L); // calls the Long version
    }
}
```

#### Access modifiers

A method can be `final`; then it can’t be overridden in subclasses.

| Modifier | Inside class | Other classes in same package | Subclasses | Other classes in other packages |
| --- | --- | --- | --- | --- |
| private | + | - | - | - |
| package-private | + | + | + in same package, - in others | - |
| protected | + | + | + | - |
| public | + | + | + | + |


