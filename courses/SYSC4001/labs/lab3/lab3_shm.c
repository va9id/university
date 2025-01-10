#include <unistd.h>
#include <stdlib.h> 
#include <stdio.h> 
#include <string.h>
#include <sys/shm.h>
#include <sys/types.h>
#include <signal.h>

#include "shm_com.h"

struct shared_use_st *shared_stuff; 

void sw(int sig){
	shared_stuff->written_by_you = 1; 
}

int main() 
{
	pid_t pid; 
	int running = 1;
	void *shared_memory = (void *)0;
	int shmid; 
	int threshold = 72; 
	
	srand((unsigned int)getpid());
	
	shmid = shmget((key_t)1234, sizeof(struct shared_use_st), 0666 | IPC_CREAT);

	if (shmid == -1) {
		fprintf(stderr, "shmget failed\n");
		exit(EXIT_FAILURE);
	} 

	shared_memory = shmat(shmid, (void *)0, 0);
	if (shared_memory == (void *)-1) {
		fprintf(stderr, "shmat failed\n");
		exit(EXIT_FAILURE); 
	}
	
	printf("Memory attached at %X\n", (int)shared_memory);

	shared_stuff = (struct shared_use_st *)shared_memory; 
	shared_stuff->written_by_you = 0;

	printf("THRESHOLD VALUE IS %d\n", threshold); 
	printf("fork is starting\n");
	pid = fork();
	
	switch(pid)
	{
	case -1: 
		perror("fork failed"); 
		exit(1); 
	case 0: 			/* child process */
		while(running) {	
			shared_stuff->random_number = rand() % 100;
			if(shared_stuff->random_number > threshold){
				kill(getppid(), SIGALRM);   
				sleep(rand() % 4);		 /* make the other process wait for us ! */
			}
			else{	
				printf("Child random number:  %d (less than threshold: %d)\n", shared_stuff->random_number, threshold); 
			}
		}
		break; 
	}
	/* parent process */

	while(running) {
		(void) signal(SIGALRM, sw);
	
		if(shared_stuff->written_by_you){
			printf("Current value greater than threshold, Parent printing from shared memory: %d\n", shared_stuff->random_number); 
			shared_stuff->written_by_you =0; 
		}
	}
	if (shmdt(shared_memory) == -1) { 	
		fprintf(stderr, "shmdt failed\n"); 
		exit(EXIT_FAILURE);
	}
	if (shmctl(shmid, IPC_RMID, 0) == -1) {
		fprintf(stderr, "shmctl(IPC_RMID) failed\n");
		exit(EXIT_FAILURE);
	}
	exit(EXIT_SUCCESS);
}
