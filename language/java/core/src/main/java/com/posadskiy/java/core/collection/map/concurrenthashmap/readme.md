### ConcurrentHashMap vs Collections.syncronizedMap()

#### ConcurrentModificationException

ConcurrentHashMap doesn't produce exception if modified while iterating.\
For objects like HashMap, performing concurrent operations is not allowed. That's why ConcurrentModificationException
recieves for synchronizedMap().

#### null-safery

ConcurrentHashMap doesn't allow null as key and value, syncronizedMap() - depends on provided map.
For HashMap or LinkedHashMap allows to one null as a key and any number of nulls as a values. For TreeMap only null
values, but not keys.

#### Performance

Obviously, ConcurrentHashMap performance is better.
