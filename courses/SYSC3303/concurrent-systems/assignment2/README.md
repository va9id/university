# Assignment 2 - UDP

## Problem Statement

Build a very basic three part system consisting of a client, an host, and a server. The client sends requests to the  host, which sends them on to the server. The server sends responses to the host, which sends them on to the client. From the client's point of view, the host appears to be the server. From the server's point of view, the host appears to be the client.

### Client Algorithm 
Client DatagramPacket is either a *read* or *write* request with the following format: 
- 0 byte
- 0 byte if **read**, or 1 byte if **write**
- filename converted from str to byte
- 0 byte
- mode converted from str to byte
- 0 byte

Client should send 11 requests to the host, alternating between read and write requests. The 11th request should be an invalid format causing the server to throw an exception when it receives it. 

### Host Algorithm 
Receives client datagram, echoes it to the server, waits for the server response, and echoes that response back to the client. 

### Server Algorithm 
Receives the client datagram from the host, parses it to ensure its validaty, generates an appropriate response, and sends the response to the host. The server response format is as follows: 
- 4 bytes: `0 3 0 1` if client request was **read**
- 4 bytes: `0 4 0 0` if client request was **write**

Server throws an exception and terminates if the received request was not in the valid Client format. 

## Usage 

Open the project in IntelliJ and run each file in the following order:

1. `Server.java`  
2. `Host.java`
3. `Client.java`

*Note:* Multiple clients can be run in seperate terminals.  