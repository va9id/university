#include <time.h>
#include <sys/time.h>
#include <stdlib.h> 
#include <stdio.h> 
#include <unistd.h>
#include <pthread.h>
#include <sys/types.h>

void *thread_function(void *arg);

int main()
{
	int res;
        pthread_t a_thread;
        void *thread_result;
	pid_t pid; 
	struct timeval start, end;
	
	//Time process creation

	gettimeofday(&start, NULL); 
	pid = fork();
	gettimeofday(&end, NULL);	
	if(pid==-1){
		printf("fork failed");
		exit(EXIT_FAILURE);
	}
	else if(pid ==0){
		exit(1); 
	}
	else{		
		printf("fork() execution time: %ld\n", ((end.tv_sec * 1000000 + end.tv_usec) - (start.tv_sec * 1000000 + start.tv_usec))); 
	}
	
	
	//Time thread creation

	gettimeofday(&start, NULL);
	res = pthread_create(&a_thread, NULL, thread_function, (void *)0);
        gettimeofday(&end, NULL);
        printf("pthread_create() execution time: %ld\n", ((end.tv_sec * 1000000 + end.tv_usec) - (start.tv_sec * 1000000 + start.tv_usec))); 

        if (res != 0) {
                perror("Thread creation failed");
                exit(EXIT_FAILURE);
        }
        res = pthread_join(a_thread, &thread_result);
        if (res != 0) {
                perror("Thread join failed");
                exit(EXIT_FAILURE);
        }
        exit(EXIT_SUCCESS);

}

void *thread_function(void *arg) {
}
