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

int main() {
	int running = 1;
	struct my_msg_st some_data; 
	int msgid;
	char buffer[BUFSIZ];

	long int server_response = 2; 

	msgid = msgget((key_t)1234, 0666 | IPC_CREAT);
	
	if (msgid == -1) {
		fprintf(stderr, "msgget failed with error: %d\n", errno); 
		exit(EXIT_FAILURE);
	}

	printf("To end client session, enter 'end'\n\n");

	while(running) {

		//Client sends a message to server

		printf("Enter some text: "); 
		fgets(buffer, BUFSIZ, stdin); 
		some_data.my_msg_type = 1; 
		strcpy(some_data.some_text, buffer);
		
		if (strncmp(buffer, "end", 3) == 0) {   //Check if client wants to end itself
                        running = 0;
                        exit(EXIT_SUCCESS);
                }


		if (msgsnd(msgid, (void *)&some_data, MAX_TEXT, 0) == -1) { 
			fprintf(stderr, "msgsnd failed\n");
			exit(EXIT_FAILURE);
		}

		//Client can get a response from the server
		if (msgrcv(msgid, (void *)&some_data, BUFSIZ, server_response, 0) == -1) {
                        fprintf(stderr, "msgrcv failed with error: %d\n", errno);
                        exit(EXIT_FAILURE);
                }

		if (strncmp(some_data.some_text, "kill", 4) == 0) { //Check if Server respones was to kill client
                        running = 0;
			exit(EXIT_SUCCESS);
                }

		if (strncmp(some_data.some_text, "endS", 4) == 0) { //Check if Server ended
                        running = 0;
		}
		//Print server response
		printf("Server response: %s", some_data.some_text);
	}
	exit(EXIT_SUCCESS); 
}
