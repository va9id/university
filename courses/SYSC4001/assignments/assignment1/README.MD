# Assignment 1: Parent, Child Processes, and Shared Memory

## Problem Statement 

Create a program that can calculate the determinant of a matrix. The parent process will fork 3 child processes that will each calculate a part of the determinant and the largest value of each row of the matrix. The results of the calculations will be stored in a shared memory structure consisting of two integer arrays D[] and L[] that all processes can access. Once child processes have completed, the parent process will print the determinant of matrix M, the largest value in the matrix, and the execution time of the child processes.
```
                             [20 20 50]
Matrix is hard coded as: M = [10  6 70]
                             [40  3  2]
```
## Usage

- Run the Makefile
    ```
    make
    ```
- Execute the executable
    ```
    ./assignment1
    ```
