#include <stdlib.h> 
#include <stdio.h> 
#include <string.h> 
#include <errno.h> 
#include <unistd.h>

#include <sys/msg.h>

#define MAX_TEXT 512

struct my_msg_st {
	long int my_msg_type; 
	char some_text[BUFSIZ];
};

int main() {

	int running = 1;
	int msgid;
	struct my_msg_st some_data; 
	long int msg_to_receive = 0;
	
	char response[BUFSIZ];

	msgid = msgget((key_t)1234, 0666 | IPC_CREAT);

	if (msgid == -1) {
		fprintf(stderr, "msgget failed with error: %d\n", errno); 
		exit(EXIT_FAILURE);
	}

	printf("To kill a client, respond to it with 'kill'\n");
	printf("To end the server, respond to a client with 'endS'\n\n");

	while(running) {
		//Server receives and displays most recent client message
		if (msgrcv(msgid, (void *)&some_data, BUFSIZ, msg_to_receive, 0) == -1) {
			fprintf(stderr, "msgrcv failed with error: %d\n", errno); 
			exit(EXIT_FAILURE);
		}
	
		printf("Client wrote: %s", some_data.some_text);
	                
		//Server can respond to client
                printf("Respond to client: ");
                fgets(response, BUFSIZ, stdin);
                some_data.my_msg_type = 2;
                strcpy(some_data.some_text, response);

                if (msgsnd(msgid, (void *)&some_data, MAX_TEXT, 0) == -1) {
                        fprintf(stderr, "msgsnd failed\n");
                	exit(EXIT_FAILURE);
                }

                //Check if server wished to end
                if (strncmp(some_data.some_text, "endS", 4) == 0) {
			running = 0;
             	}

	}
	
	if (msgctl(msgid, IPC_RMID, 0) == -1) { 
		fprintf(stderr, "msgctl(IPC_RMID) failed\n"); 
		exit(EXIT_FAILURE);
	}
	exit(EXIT_SUCCESS); 
}
