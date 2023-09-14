## Database

##### Denormalized vs. Normalized Databases

Normalized databases are designed to minimize redundancy, while denormalized databases are designed to optimize read
time.

#### Small database

Small database should be normalized.

#### Large database design

When designing a large, scalable database, joins are generally very slow.
Thus, you must denormalize your data. Think carefully about how data will be used-you'll probably need to duplicate
the data in multiple tables.

#### Join types

- INNER JOIN: The result set would contain only the data where the criteria match
- LEFT OUTER JOIN, or simply LEFT JOIN: The result will contain all records from the left table. If no matching records
  were found in the right table, then its fields will contain the NULL values.
- RIGHT OUTER JOIN, or simply RIGHT JOIN: This type of join is the opposite ofLEFT JOIN. It will contain every record
  from the right table; the missing fields from the left table will be NULL.
- FULL OUTER JOIN: This type of join combines the results of the LEFT and RIGHT JOINS. All records from both tables will
  be included in the result set, regardless of whether or not a matching record exists in the other table. If no
  matching record was found, then the corresponding result fields will have a NULL value.

#### Denormalization

Denormalization is a database optimization technique in which we add redundant data to one or more tables. This can help
us avoid costly joins in a relational database.

Cons:

- Updates and inserts are more expensive.
- Denormalization can make update and insert code harder to write.
- Data may be inconsistent. Which is the "correct" value for a piece of data?
- Data redundancy requires more storage.

Pros:

- Retrieving data is faster since we do fewer joins.
- Queries to retrieve can be simpler (and therefore less likely to have bugs), since we need to look at fewer tables.

#### ACID

- atomicity - all queries of transaction should be committed or no one from them.
- consistency - after transaction, database moves from consistent state to consistent state.
- isolation - should be no affect on transaction from other transactions.
- durability - after transaction commit, data should be stored in database safety even if failure of the database
  happens.

#### Parallel transactions problems

A - first transaction. B - second transaction. Both of them are concurrent.

- lost update - A and B write. A edit some data, B edit the same data (but can't see what A changed, because it isn't
  committed yet), then A commit and then B commit. As a result - B rewrite data saved by A.
- dirty read - A writes, B reads. A writes but not commit, then B reads the same data, but then A
  rollback and B has data that absent in database.
- non-repeatable read [update] - A writes, B reads. A reads row, then B changes this row and commit, then B reads this
  row again.
  As a result, second read could provide different row than first read.
- phantom read [insert, delete] - A writes, B reads. B reads list of rows, then A commits, then B reads this list again.
  As a result, B
  could get less or more rows for second read.
- serialization anomaly - Few transactions committed concurrently, but result is different that after any of
  consistently transactions sequence. Example - A and B applied concurrently, but result is different from A -> B or
  B -> A.

#### SQL isolation levels

1. Read uncommitted - dirty read - yes, according to PostgreSQL - no
2. Read committed - dirty read - no, transaction can see only its own updates during life.
3. Repeatable read - no dirty read, no non-repeatable read. For PostgreSQL - no phantom read.
4. Serialization - nothing

#### Blocking

DBMS allows to set block on table
SELECT * FROM table_tame WHERE column_name = 'some text' FOR UPDATE;

#### Deadlock

Deadlocks can happen in multi-user environments when two or more transactions are running concurrently and try to access
the same data in a different order. When this happens, one transaction may hold a lock on a resource that another
transaction needs, while the second transaction may hold a lock on a resource that the first transaction needs. Both
transactions are then blocked, waiting for the other to release the resource they need.

#### Index

A database index is a data structure that improves the speed of data retrieval operations on a database table at the
cost of additional writes and storage space to maintain the index data structure.

- fast lookup
- policing the database constraints

Types:

- В-tree
- Hash
- GiST
- SP-GiST
- GIN
- BRIN

- One column index
- Two or more column index
- Unique index
- Statement-based index - `(lower(full_name))`
- Partly index - `where id > 10000`

#### Query optimization

- Modify query code
- Reset pg_statistic - typically, pg_stat_reset is used when you want to clear existing statistics and start afresh.
  This might be necessary for several reasons:
    - After a significant database event such as an update, migration, or other major changes.
    - When troubleshooting performance issues, and you want to clear out previous statistics to isolate the problem.
    - When you want to measure the impact of a particular action or set of actions on the database.
- Denormalization - creating temporary tables (such cache) or indexes.
- Adjust planning parameters that are responsible for rows concatenation.
- Adjust planning parameters that are responsible for method for data access (enable_seqscan, enable_indexscan, enable_indexonlyscan, enable_bitmapscan)
- Adjust planning parameters that are responsible for concatenation sets of rows (enable_nestloop, enable_hashjoin, enable_mergejoin);
- Adjust planning parameters that are responsible for aggregation on hash, temporary rows, explicit soring.

#### Scalability

**Shard** - A database shard, or simply a shard, is a horizontal partition of data in a database or search engine. Each shard is
held on a separate database server instance, to spread load.
**Replication** - storing copy of instance on another cloud.

#### Commands

TRUNCATE - removes all rows from a table. The operation cannot be rolled back and no triggers will be fired. As such,
TRUNCATE is faster and doesn't use as much undo space as a DELETE. Table level lock will be added when Truncating.

DELETE - removes rows from a table. A WHERE clause can be used to only remove some rows. If no WHERE condition is
specified, all rows will be removed. After performing a DELETE operation you need to COMMIT or ROLLBACK the transaction
to make the change permanent or to undo it. Note that this operation will cause all DELETE triggers on the table to
fire. Row level lock will be added when deleting.

EXPLAIN - display the plan of query

ANALYZE - run the query and provide analysis
