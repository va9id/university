// SYSC 4001 - Assignment 3

#include <stdlib.h> 
#include <stdio.h> 
#include <string.h> 
#include <errno.h> 
#include <unistd.h>
#include <sys/msg.h>
#include <time.h> 
#include <sys/time.h> 

#define MAX_TEXT 512
#define MAX_SIZE 100 

int numbers[MAX_SIZE] = {0};
int largest_index = 0; 

struct my_msg_st {
	long int my_msg_type; 
	char some_text[BUFSIZ];
};

/* Helper function to swap array indices when sorting */
void swap(int x, int y){
	int temp = numbers[x]; 
	numbers[x] = numbers[y]; 
	numbers[y] = temp; 
}

/* Bubble sort algorithm to sort array in ascending order */
void sort(){
	for(int i=0; i < largest_index - 1; i++){
		for(int j=0; j < largest_index - i - 1; j++){
			if(numbers[j] > numbers[j+1]){
				swap(j, j+1); 
			}
		}
	}
}

/* Function returns the sum of all elements added to the array */ 
int sum(){
	int sum = 0; 
	for(int i=0; i < largest_index; i++){
		sum += numbers[i]; 
	}
	return sum;
}

/* Returns the average value of all elements added to the array */ 
float average(){
	int s = sum();
	float avg = (float) s / (float) largest_index; 
	return avg; 
}

/* Returns the mimimum value of all elements added to the array */ 
int min(){
	int min = numbers[0];
	for(int i=1; i<largest_index;i++){
		if(numbers[i] < min){
			min = numbers[i]; 
		}
	} 
	return min;
}

/* Removes every occurence of a given number from the array */ 
void removeElem(int del){
	int i=0; 
	while(i < largest_index){
		if(numbers[i] == del){
			for(int j=i; j< largest_index - 1; j++){
				numbers[j] = numbers[j+1]; 
			}
			largest_index--; 
		}
		else{
			i++; 
		}
	}
}

/* Function that inserts an element into the array */
int insert(int num){
	if(largest_index >= MAX_SIZE){ //Inserting into full array (not possible)
		return 0; 
	}
	else{
		numbers[largest_index] = num; 
		largest_index++; 
		return 1; 
	}
}

/* Returns true is array is empty, false otherwise */ 
int isEmpty(){
	if(largest_index == 0){
		return 1;
	}
	else{
		return 0;
	}
}


/* Main function that creates messages queues to communicate to and from the user */ 
int main() {

	int running = 1;
	int msgid, msgid2;
	struct my_msg_st some_data; 
	some_data.my_msg_type = 1;
	long int msg_to_receive = 1;
	struct timeval start, end; 
	long int totalTime = 0; 
	int totalCommands = 0; 
	
	//Calculator sends messages on this queue
	msgid2 = msgget((key_t)321, 0666 | IPC_CREAT);
	if (msgid2 == -1) {
                fprintf(stderr, "msgget failed with error: %d\n", errno);
                exit(EXIT_FAILURE);
        }

	//Calculator recieves messages on this queue
	msgid = msgget((key_t)123, 0666 | IPC_CREAT);	
	if (msgid == -1) {
		fprintf(stderr, "msgget failed with error: %d\n", errno); 
		exit(EXIT_FAILURE);
	}
	
	
	while(running) {
		char reply[BUFSIZ];
		printf("Waiting for user ...\n"); 

		//Server receives and displays most recent client message

		if (msgrcv(msgid, (void *)&some_data, BUFSIZ, msg_to_receive, 0) == -1) {
			fprintf(stderr, "msgrcv failed with error: %d\n", errno); 
			exit(EXIT_FAILURE);
		}
		printf("Client entered the command: %s", some_data.some_text);
	        

		//Check what command came from user

		if(strncmp(some_data.some_text, "End", 3) ==0) {
			if(totalCommands > 0) printf("Average computation time of each command: %ld usec\n", totalTime/totalCommands);  	
			running = 0;
		}
		else if (strncmp(some_data.some_text, "Sum", 3) == 0) { //Sum command 
			gettimeofday(&start, NULL); 
			printf("Computing SUM of data set and sending result to user\n"); 
			int s = sum();
			if(isEmpty()) strcpy(reply, "No Sum, data set is empty\n");
			else sprintf(reply, "Sum of current data set = %d\n", s); 	
			gettimeofday(&end, NULL); 
			totalTime += ((end.tv_sec * 1000000 + end.tv_usec) - (start.tv_sec * 1000000 + start.tv_usec)); 
			totalCommands++; 
		}
                else if (strncmp(some_data.some_text, "Average", 3) == 0) { //Average command 
			gettimeofday(&start, NULL); 
			printf("Computing AVERAGE of data set and sending result to user\n"); 
			float avg  = average();
			if(isEmpty()) strcpy(reply, "No Average, data set is empty\n");
			else sprintf(reply, "Average of current data set is = %.3f\n", avg);
                	gettimeofday(&end, NULL); 
			totalTime += ((end.tv_sec * 1000000 + end.tv_usec) - (start.tv_sec * 1000000 + start.tv_usec));
                        totalCommands++; 
		}
                else if (strncmp(some_data.some_text, "Min", 3) == 0) { //Minimum value command	
			gettimeofday(&start, NULL); 
			printf("Computing MIN of data set and sending result to user\n"); 
			int m  = min();
			if(isEmpty()) strcpy(reply, "No Min, data set is empty\n");
			else sprintf(reply, "Minimum value of current data set is = %d\n", m);
		 	gettimeofday(&end, NULL); 	
			totalTime += ((end.tv_sec * 1000000 + end.tv_usec) - (start.tv_sec * 1000000 + start.tv_usec));
                        totalCommands++; 
		}
                else if (strncmp(some_data.some_text, "Median", 3) == 0) { //median command 
			gettimeofday(&start, NULL); 
			printf("Computing MEDIAN of data set and sending result to user\n"); 
			sort(); 
			int middle = largest_index / 2;
			int firstMidValue = numbers[middle-1];
                        int secondMidValue = numbers[middle]; 
			if(largest_index % 2 == 0){ //even length
				if(isEmpty()) {
					strcpy(reply, "No Median, data set is empty\n"); 
				}
				else{ 
					sprintf(reply, "Median of data set is %d and %d\n", firstMidValue, secondMidValue);  
				}
			}
			else{ //odd length 
				sprintf(reply, "Median of data set is = %d\n", secondMidValue);  
			}
			gettimeofday(&end, NULL); 
			totalTime += ((end.tv_sec * 1000000 + end.tv_usec) - (start.tv_sec * 1000000 + start.tv_usec));
                        totalCommands++; 
                }
		else{ //Either invalid command, insert, delete			
			char *cmd = strtok(some_data.some_text, " ");
			if(strncmp(some_data.some_text, "Delete", 6) ==0){ //delete command 
				gettimeofday(&start, NULL); 
				printf("DELETING integer from data set\n"); 
				cmd = strtok(NULL, " "); 
				int num = atoi(cmd); 
				removeElem(num); 
				sprintf(reply, "All occurences of %d were removed from the data set\n", num); 
			 	gettimeofday(&end, NULL); 	
				totalTime += ((end.tv_sec * 1000000 + end.tv_usec) - (start.tv_sec * 1000000 + start.tv_usec));
                        	totalCommands++; 
			}
			else if(strncmp(some_data.some_text, "Insert", 6)==0){ //Insert command 
				gettimeofday(&start, NULL);
				printf("INSERTING integer into data set\n"); 
				cmd = strtok(NULL, " "); 
				int num = atoi(cmd); 
    				int inserted = insert(num);
				if(inserted) sprintf(reply, "Number %d was added to the data set\n", num);
				else strcpy(reply, "Could not insert, data set has reached max capcity\n"); 
				gettimeofday(&end, NULL); 
				totalTime += ((end.tv_sec * 1000000 + end.tv_usec) - (start.tv_sec * 1000000 + start.tv_usec));
                        	totalCommands++; 
			}	
			else{ //Invalid command 
				printf("User entered an INVALID command\n");
				strcpy(reply, "Please enter a valid command\n");
			}
		}

		//Calculator sends response to user 

		strcpy(some_data.some_text, reply); 
		if(msgsnd(msgid2, (void *)&some_data, MAX_TEXT, 0) == -1) {
			fprintf(stderr, "msgsnd failed\n");
                        exit(EXIT_FAILURE);
		}
	}

	//Delete message queues 

	if (msgctl(msgid, IPC_RMID, 0) == -1) { 
		fprintf(stderr, "msgctl(IPC_RMID) failed\n"); 
		exit(EXIT_FAILURE);
	}
	if (msgctl(msgid2, IPC_RMID, 0) == -1) {
                fprintf(stderr, "msgctl(IPC_RMID) failed\n");
                exit(EXIT_FAILURE);
        }
	exit(EXIT_SUCCESS); 
}

