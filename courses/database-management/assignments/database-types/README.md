# Nested and Object Relational Databases Assignment

## Problem Statement 

Generate Nested relational databases and Object relational databases using SQL. The object relational database should have just 2 object tables (Course, Person), where Person is a substitutable table. Use the IDs of persons for their relationships with Course. After creating the tables, express the following queries using SQL: 

1. List course together with the students taking the relation.
    - Results: {PL: Alan, Barb}, {OS: Alan, Last}, {DB: Alan, Barb, Last, Dora}
2. List instructors together with the students they teach in all courses/sections in a nested table.
    - Results: {Jack: Alan, Barb}, {Lisa: Alan, Dora, Last}
3. List instructors together with the students they teach in all courses/sections in a nested table such that instructor and students have the same birth month.
    - Results: {Jack: Alan, Barb}, {Lisa: Dora, Last}
4. List students taking all course.
    - Results: Alan

## Usage 
- Run `reset.sql` to remove any pre-existing tables or types that may interfere with the assignment
    - ***Reminder***: Repeat this step after running either `nested_databases` or `object_databases`
- Generate the databases in the following order:
    1. `person.sql`
    2. `course.sql`

