/* SYSC 4001 Assignment 1 */

#include <unistd.h>
#include <stdlib.h> 
#include <stdio.h> 
#include <string.h>
#include <sys/shm.h>
#include <sys/types.h>
#include <signal.h>
#include <time.h>
#include <sys/time.h>

#include "shm_com.h"

#define MICRO_SEC_IN_SEC 1000000


struct shared_use_st *shared_stuff; 

int M[3][3] = { {20, 20, 50}, {10, 6, 70}, {40, 3, 2} }; //Matrix 

/*
Function calculates each part (D1, D2, D3) of the determinant given the index i.
Index i is between the values {0, 1, 2}, corresponding to the child process that 
invoked the function. Result of the partial determinant is stored in shared memory. 
*/
void calculateD(int i){
	int d_value; 
	if(i==0){
		d_value = M[0][0] * ( ( M[1][1] * M[2][2] ) - ( M[1][2] * M[2][1] ) ); 
	}
	if(i==1){
		d_value = M[0][1] * ( ( M[1][2] * M[2][0] ) - ( M[1][0] * M[2][2] ) ); 
	}
	if(i==2){
		d_value = M[0][2] * ( ( M[1][0] * M[2][1]) - (M[1][1] * M[2][0] ) ); 
	}
	shared_stuff->D[i] = d_value; 
}

/*
Function that calculates the largest value in row i of the matrix and stores
the result in shared memory.
*/
void calculateR(int i){
	int maximum_row_value = M[i][0]; 	
	for(int j = 1; j < 3; j++){
		if(M[i][j] > maximum_row_value){
			maximum_row_value = M[i][j]; 
		}
	}
	shared_stuff->L[i] = maximum_row_value;  
}

/*
Calculates the largest value in the matrix. Finds the largest value from the
shared memory array L, which stores the largest value of each row of the matrix. 
*/
int largestVal(){
	int largest = shared_stuff->L[0]; 
	for(int i = 0; i < 3; i++){
		if(shared_stuff->L[i] > largest){
			largest = shared_stuff->L[i];
		}
	}
	return largest; 
}

/*
Calculates the determinant of the matrix M. Adds all the partial determinants 
together from the shared memory array D. 
*/
int determinant(){
	int det = 0; 
	for(int i = 0; i < 3; i++){
		det += shared_stuff->D[i];
	}
	return det; 
}


/*
Creates the shared memory used by parent and child process. Three child processes 
are forked to each calculate one piece of the determinant of matrix M. Once completed, 
the parent process prints the determinant of the matrix along, largest value in the 
matrix, and the time it took for all child processes to execute.
*/
int main() 
{
	printf("\nAssignment 1 - SYSC 4001\n");
	
	pid_t pid; 
	void *shared_memory = (void *)0;
	int shmid; 
	int threshold = 72; 	
	int i;
	struct timeval start, end;

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
	
	printf("Shared memory attached at %X\n", (int)shared_memory);

	shared_stuff = (struct shared_use_st *)shared_memory; 
	
	gettimeofday(&start, NULL); //Time before first fork() is invoked
 
	for(i = 0; i < 3; i++){	//Loop will fork and create 3 child processes
	
		pid = fork();
		if(pid == -1){		//Fork fails
			perror("fork failed"); 
			exit(1); 
		}
		if(pid == 0){		//Child process
			printf("Child Process: working with element %d of D\n", i);
			calculateD(i); 
			calculateR(i);	 	
			exit(1); 
		}		
	}

	gettimeofday(&end, NULL); //Child processes are done and parent is executed

	//Parent Process
	int k = 3; 	
	while(k > 0){
		wait(NULL);
		k--; 
	}
	
	printf("Parent process is priting the determinant of matrix M: %d\n", determinant() ); 
	printf("Parent process is printing largest integer in matrix M: %d\n", largestVal() ); 
	printf("Time required to perform all operations: %ld mirco sec\n\n", ( (end.tv_sec * MICRO_SEC_IN_SEC + end.tv_usec) - (start.tv_sec * MICRO_SEC_IN_SEC + start.tv_usec) ) );

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
