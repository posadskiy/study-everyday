#### Index

A database index is a data structure that improves the speed of data retrieval operations on a database table at the
cost of additional writes and storage space to maintain the index data structure.

- fast lookup
- enforcing database constraints

Types:

- B-tree
- Hash
- GiST
- SP-GiST
- GIN
- BRIN

- Single-column index
- Multi-column index
- Unique index
- Expression index — e.g. `(lower(full_name))`
- Partial index — e.g. `where id > 10000`


