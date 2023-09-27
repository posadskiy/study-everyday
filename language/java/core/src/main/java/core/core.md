### Core

#### Anonymous Inner Class

Anonymous Inner Class provides class defenition and and instance at the same time

```java
Thread t1=new Thread(new Runnable(){
@Override
public void run(){
    // something
    }
    });
    t1.start();
```

###### this

`this` reffers to Anonymous Inner Class
`this` reffers to class, that includes lambda

###### Compilation

On compilation, for each anonymous class, a separate class file gets generated.

In the case of a lambda expression, an `invokedynamic` instruction is added to the class file. This opcode instruction
helps to figure out the functional interface method to be called.
Also, an equivalent private static or non-static method is added to the bytecode. The signature of this method matches
the functional interface method.

###### Performance

Better for lambda - no new class creationg.
