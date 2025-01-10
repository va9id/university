#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>


#include <sys/sem.h>

#include "semun.h"


static int set_semvalue(void);
static void del_semvalue(void); 
static int semaphore_p(void);
static int semaphore_v(void); 

static int sem_id; 


int main(int argc, char *argv[])
{
	int i; 
	int pause_time; 
	
	srand((unsigned int)getpid()); 
	
	sem_id = semget((key_t)1234, 1, 0666 | IPC_CREAT); 

	if (argc > 1) {
		if (!set_semvalue()) {
			fprintf(stderr, "Failed to intialize semaphore\n");
			exit(EXIT_FAILURE);
		}
		if(!semaphore_p()) exit(EXIT_FAILURE);
		if(!semaphore_p()) exit(EXIT_FAILURE);
                printf("Process 1 statement\n");
      	        fflush(stdout);
		sleep(rand()%5);	
		if(!semaphore_v()) exit(EXIT_FAILURE);		
	}else{
		printf("Process 2 statement\n"); 
		fflush(stdout); 
		if(!semaphore_v()) exit(EXIT_FAILURE);		
	}
	
	printf("\n%d - finshed\n", getpid()); 
	if (argc > 1) {
		sleep(5);
		del_semvalue(); 	
	}

	exit(EXIT_SUCCESS); 
}
static int set_semvalue(void)
{

	union semun sem_union; 
	
	sem_union.val = 1; 
	if(semctl(sem_id, 0, SETVAL, sem_union) == -1) return(0);
	return(1); 
}

static void del_semvalue(void)
{
	union semun sem_union; 

	if(semctl(sem_id, 0, IPC_RMID, sem_union) == -1)
		fprintf(stderr, "Failed to delete semaphore\n"); 		
}

static int semaphore_p(void)
{
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

static int semaphore_v(void){

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
