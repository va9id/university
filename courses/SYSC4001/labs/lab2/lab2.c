#include <time.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

#define MICRO_SEC_IN_SEC 1000000

int main()
{
	pid_t pid; 
	char *message; 
	int n;
	struct timeval start, end; 
	
	printf("fork program starting\n");
	gettimeofday(&start, NULL);
	pid = fork(); 
	gettimeofday(&end, NULL); 
	switch(pid)
	{
	case -1: 
		perror("fork failed");
		exit(1); 
	case 0: 
		message = "This is the child";
		printf("child process: the value returned by fork is %d\n", getppid());
		pid = fork();
		n =10; 
		break; 
	default: 
		message = "This is the parent";
		printf("PID is: %d\n", pid); 
		printf("Start time: %lf sec from Epoch\n", start.tv_sec + (double)start.tv_usec/MICRO_SEC_IN_SEC);	
		printf("End time: %lf sec from Epoch\n", end.tv_sec + (double)end.tv_usec/MICRO_SEC_IN_SEC);	
		printf("\nElapsed time: %ld micro sec\n", ((end.tv_sec * MICRO_SEC_IN_SEC + end.tv_usec) - (start.tv_sec * MICRO_SEC_IN_SEC + start.tv_usec )));
		n=6; 
		break;
	} 
	for(;n>0;n--){
		puts(message);
		sleep(1);	
	}
	exit(0);
}
