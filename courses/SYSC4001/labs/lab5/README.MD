# Lab 5: Message Queues

## Problem Statement 

Modify both programs `msg1.c` and `msg2.c` to use only ONE message queue for one server and multiple clients.

## Usage

- Compile `msg1.c` and `msg2.c`
    ``` 
    gcc -c msg1.c msg2.c 
    ```

- Compile the client, `client.c`
    ``` 
    gcc -o client client.c
    ```
- Compile the server, `server.c`
    ``` 
    gcc -o server server.c
    ```
- In one terminal, execute the `server`
    ```
    ./server
    ```
- In other terminal(s), execute `client(s)`
    ```
    ./client
    ```