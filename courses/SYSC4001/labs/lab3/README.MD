# Lab 3: Shared Memory

## Problem Statement 

The program creates a child using `fork()`. Add a piece of shared memory for both the parent and child processes. The child process continuously generates a random number and puts it in the shared memory. If the random number is over a pre-configured threshold, the child process sends a signal to the parent which reads the current value from the shared memory.

## Usage

- Run the Makefile
    ```
    make
    ```
- Execute the executable
    ```
    ./myapp
    ```