# Lab 4: Semaphores

## Problem Statement 

A semaphore can be initialized to a different value, e.g., k in the circular buffer for the producer/consumer problem. Semaphores can be used for synchronization. For instance, consider two processes, P1 and P2. P1 has to wait until P2 finishes statement S2 before P1 can execute statement S1. Implement the example using the system semaphore functions. S1 and S2 can be just printf() statements. Run the program several times and ensure that no race conditions are happening.

## Usage

- Run the Makefile
    ```
    make
    ```
- Execute the executable
    ```
    ./myapp
    ```