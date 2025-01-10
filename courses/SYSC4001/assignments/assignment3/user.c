// SYSC 4001 - Assignment 3

#include <stdlib.h> 
#include <stdio.h> 
#include <string.h> 
#include <errno.h> 
#include <unistd.h>

#include <sys/msg.h>

#define MAX_TEXT 512

struct my_msg_st {
	long int my_msg_type; 
	char some_text[MAX_TEXT];
};

/* Prints list of commands to user's console */ 

void printCommands(){
	printf("List of commands: {Insert NUMBER, Delete NUMBER, Sum, Average, Min, Median, End}\n");
}

/* Main function that gets user input, sends it to calculator in message queue, receives responsef from message queue */
int main() {
	int running = 1;
	struct my_msg_st some_data; 
	int msgid, msgid2;
	char buffer[BUFSIZ];
	long int server_response = 1; 

	//User sends messages on this queue 	
	msgid = msgget((key_t)123, 0666 | IPC_CREAT); 
	if (msgid == -1) {
                fprintf(stderr, "msgget failed with error: %d\n", errno);
                exit(EXIT_FAILURE);
        }

	//User receives messages on this queue
	msgid2 = msgget((key_t)321, 0666 | IPC_CREAT); 
	if (msgid2 == -1) {
		fprintf(stderr, "msgget failed with error: %d\n", errno); 
		exit(EXIT_FAILURE);
	}

	printf("Assignment 3 - SYSC 4504\n"); 
	printCommands(); 
	printf("----------------------------------------\n");

	while(running) {

		//Client sends a message to server

		printf("Enter a command (enter 'help' to see command list): "); 
		fgets(buffer, BUFSIZ, stdin); 
		some_data.my_msg_type = 1; 
		strcpy(some_data.some_text, buffer);

                if (msgsnd(msgid, (void *)&some_data, MAX_TEXT, 0) == -1) {
                        fprintf(stderr, "msgsnd failed\n");
                        exit(EXIT_FAILURE);
                }

		if (strncmp(buffer, "End", 3) == 0) {   //Check if client wants to end itself
                        running = 0;
                        exit(EXIT_SUCCESS);
                }
		if (strncmp(buffer, "help", 4) == 0) {   //prints list of commands to the user's console
			printCommands(); 
		}

		//User gets response from the calculator
		if (msgrcv(msgid2, (void *)&some_data, BUFSIZ, server_response, 0) == -1) {
                        fprintf(stderr, "msgrcv failed with error: %d\n", errno);
                        exit(EXIT_FAILURE);
                }

		//Print server response to command 
		printf("Calculator: %s", some_data.some_text);
	}
	exit(EXIT_SUCCESS); 
}
