### @Transactional

Spring annotation @Transactional replace the next code

```java
    Connection connection=DriverManager.getConnection(...);
    try {
        connection.setAutoCommit(false);
        PreparedStatement firstStatement=connection.prepareStatement(...);
    
        firstStatement.executeUpdate();
    
        PreparedStatement secondStatement=connection.prepareStatement(...);
    
        secondStatement.executeUpdate();
        connection.commit();
    } catch (Exception e) {
        connection.rollback();
    }
```

##### isolation

- DEFAULT - uses isolation level from data store.
- READ_UNCOMMITTED - dirty reads, non-repeatable reads, and phantom reads can occur.
- READ_COMMITTED - dirty reads are prevented; non-repeatable reads and phantom reads can occur.
- REPEATABLE_READ - dirty reads and non-repeatable reads are prevented; phantom reads can occur.
- SERIALIZABLE - dirty reads, non-repeatable reads, and phantom reads are prevented.

##### propagation

- REQUIRED - Support a current transaction, create a new one if none exists.
- SUPPORTS - Support a current transaction, execute non-transactionally if none exists.
- MANDATORY - Support a current transaction, throw an exception if none exists.
- REQUIRES_NEW - Create a new transaction, and suspend the current transaction if one exists.
- NOT_SUPPORTED - Execute non-transactionally, suspend the current transaction if one exists.
- NEVER - execute non-transactionally, throw an exception if a transaction exists.
- NESTED - Execute within a nested transaction if a current transaction exists, behave like REQUIRED otherwise.

##### timeout

The timeout for this transaction (in seconds). -1 by default.

##### read-only

A boolean flag that can be set to true if the transaction is effectively read-only, allowing for corresponding
optimizations at runtime. False by default.

##### rollbackFor

Defines zero (0) or more exception types, which must be subclasses of Throwable, indicating which exception types must
cause a transaction rollback.

##### noRollbackFor

Defines zero (0) or more exception types, which must be subclasses of Throwable, indicating which exception types must 
**not** cause a transaction rollback.
