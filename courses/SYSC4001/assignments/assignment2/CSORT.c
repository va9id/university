// SYSC 4001 - Assignment 2

#include <unistd.h>
#include <stdlib.h> 
#include <stdio.h> 
#include <string.h>
#include <sys/shm.h>
#include <sys/types.h>
#include <signal.h>
#include <time.h>
#include <sys/time.h>
#include <stdbool.h>
#include <sys/sem.h>

#include "shm.h"
#include "semun.h"

#define SIZE 7

static int set_semvalue(int);
static void del_semvalue(int); 
static int semaphore_p(int);
static int semaphore_v(int); 

static int sem_index2;
static int sem_index4; 
struct shared_use_st *shared_stuff; 
bool debug = false;

/* Function to print the array */
void printAR(){
	printf("[ ");
	for(int i=0; i < SIZE; i++){
		printf("%c ", shared_stuff->AR[i]);	
	}
	printf("]\n");
}

/* Function to convert all array indexes to lower case chars */
void lowerCase(){
	for(int i=0; i<SIZE; i++){
		shared_stuff->AR[i] = tolower(shared_stuff->AR[i]); 
	}
}

/* Function to check if array is sorted */
bool isSorted(){
	for(int i=0; i < SIZE - 1 ; i++){
		if(shared_stuff->AR[i] > shared_stuff->AR[i+1]){
			return false; 
		}
	}
	return true;
}

/* Function that swaps two elements */
void swap(int i, int j){
        char temp = shared_stuff->AR[i];
        shared_stuff->AR[i] = shared_stuff->AR[j];
        shared_stuff->AR[j] = temp;
}

/* Function that sorts the array */
void sortAR(int process){
	while(!isSorted()){
		if(process == 1){
			for(int i = 0; i < 2; i++){
				if(shared_stuff->AR[i] > shared_stuff->AR[i+1]){
					if(debug){ printf("Process P%d: performed swapping\n", process);}
					if(i == 0){
						swap(i, i+1); 	
					}
					else{	//AR[1] swaps with AR[2] 
						semaphore_p(sem_index2);
						swap(i, i+1);
						semaphore_v(sem_index2);
					}			
				}
				else{
					if(debug) {printf("Process P%d: No swapping\n", process);}
				}
			}
		}
		else if(process == 2){
        	        for(int i = 2; i < 4; i++){
				if(shared_stuff->AR[i] > shared_stuff->AR[i+1]){
                     		        if(debug) {printf("Process P%d: performed swapping\n", process);} 
					if(i == 2){
                             		        semaphore_p(sem_index2);
                               		        swap(i, i+1);
             	                   	        semaphore_v(sem_index2); 
             		                }
                      		        else{	//AR[3] swaps with AR[4]
                           		        semaphore_p(sem_index4);
              	                                swap(i, i+1);
                       	                        semaphore_v(sem_index4);
					}       
				}
				else{
                                        if(debug){ printf("Process P%d: No swapping\n", process);}
                                }
          	        }
          	}
		else{	//process == 3
		        for(int i = 4; i < 6; i++){
				if(shared_stuff->AR[i] > shared_stuff->AR[i+1]){
        	                	if(debug){ printf("Process P%d: perfomed swapping\n", process);}
				        if(i == 4){
               		                        semaphore_p(sem_index4);
                       		                swap(i, i+1);
	                       	                semaphore_v(sem_index4);
       	             	                }
               	        	        else{   //AR[5] swaps with AR[6]
                       	                        swap(i, i+1);
					}
				}
			 	else{
                                        if(debug) {printf("Process P%d: No swapping\n", process);}
                                }
			}
		}
	}	
}

/* Main function that runs the program */ 
int main()
{
	pid_t pid; 
	void *shared_memory = (void *)0;
	int shmid;
	char response;

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

	sem_index2 = semget((key_t)123, 1, 0666 | IPC_CREAT);
        sem_index4 = semget((key_t)456, 1, 0666 | IPC_CREAT);
	
	if(!set_semvalue(sem_index2)){
		fprintf(stderr, "Failed to intialize first semaphore\n");
		exit(EXIT_FAILURE);
	}

	if(!set_semvalue(sem_index4)){
                fprintf(stderr, "Failed to intialize second semaphore\n");
                exit(EXIT_FAILURE);
        }

	printf("Lexicographic Sort\n"); 
	printf("Shared memory attached at %X\n", (int)shared_memory);

	shared_stuff = (struct shared_use_st *)shared_memory; 

	/* Get user input array and store it in shared memory */ 
	for(int i = 0; i < SIZE; i++){
		printf("Enter character %d: ", i); 
		scanf(" %c", &shared_stuff->AR[i]); 
	}
	lowerCase(); 
	printf("Inputted Array: "); 
	printAR(); 
	
	//Ask for debug mode
	printf("Do you want to run in DEBUG mode (y/n): ");
	scanf(" %c", &response); 
	if (response == 'y'){
		debug = true; 
	}

	/* Create 3 child processes to sort the array */
	for(int i = 1; i < 4; i++){
		pid = fork();
		if(pid == -1){ 
			perror("fork failed");
			exit(1);
		} 
		if(pid == 0){ 
			sortAR(i);
			exit(1);
		}
	} 
	int l =3; 
	while(l > 0){
		wait(NULL);
		l--;
	}

	printf("--------------------\n");
 	printf("Sorted Array: ");
	printAR();

	/* Code to delete shared memory and semaphores */

	if (shmdt(shared_memory) == -1) { 	
		fprintf(stderr, "shmdt failed\n"); 
		exit(EXIT_FAILURE);
	}
	if (shmctl(shmid, IPC_RMID, 0) == -1) {
		fprintf(stderr, "shmctl(IPC_RMID) failed\n");
		exit(EXIT_FAILURE);
	}

	del_semvalue(sem_index2);
	del_semvalue(sem_index4); 
	
	exit(EXIT_SUCCESS);

}



static int set_semvalue(int sem_id){
	union semun sem_union; 
	sem_union.val = 1; 
	if(semctl(sem_id, 0, SETVAL, sem_union) == -1) return(0);
	return(1); 
}

static void del_semvalue(int sem_id){
	union semun sem_union; 
	if(semctl(sem_id, 0, IPC_RMID, sem_union) == -1)
		fprintf(stderr, "Failed to delete semaphore\n"); 		
}

static int semaphore_p(int sem_id){
	struct sembuf sem_b; 
	sem_b.sem_num = 0;
	sem_b.sem_op = -1; /* P() */
	sem_b.sem_flg = SEM_UNDO; 
	if(semop(sem_id, &sem_b, 1) == -1){
		fprintf(stderr, "sempahore_p failed\n"); 
		return(0); 	
	} 
	return(1); 
}

static int semaphore_v(int sem_id){
	struct sembuf sem_b; 
	sem_b.sem_num = 0;
	sem_b.sem_op = 1; /* V() */
	sem_b.sem_flg = SEM_UNDO; 
	if(semop(sem_id, &sem_b, 1) == -1){
		fprintf(stderr, "sempahore_v failed\n"); 
		return(0); 	
	} 
	return(1); 
}
