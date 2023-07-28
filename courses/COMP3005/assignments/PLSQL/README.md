# Procedural Language SQL (PLSQL) Assignment

PLSQL is a procedural langugae that increases the expresiveness of SQL and processes query results in a tuple-oriented mannger. It allows optimizations by combining SQL statements and is used to develop modular database application programs. 

## Problem Statement 

Generate the necessary [database tables](/assignments/PLSQL/databases/) for this assignment. 

### Problem 1
Using PLSQL list all customer rows, in customer number order so that each customer row is immediately followed in the listing by all bank rows for banks that the customer has account in, in bank number order. Customers who do not bank should still be listed.

### Problem 2
Repeat Problem 1 using parameterized cursor that takes a customer name. It should first prompt the user to enter a customer name and then display the same information as in 3 just for the given customer. Ensure you test this program.

## Usage 
Generate the [databases](/assignments/PLSQL/databases/) in the following order: 
1. `bank.sql`
2. `customer.sql`
3. `account.sql`
