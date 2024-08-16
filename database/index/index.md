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
