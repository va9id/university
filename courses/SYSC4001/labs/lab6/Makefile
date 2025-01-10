
all: thread1 thread3 thread4 thread7 thread8 looptime nolooptime

thread1: thread1.c
	cc -D_REENTRANT thread1.c -o thread1 -lpthread

thread3: thread3.c
	cc -D_REENTRANT thread3.c -o thread3 -lpthread

thread4: thread4.c
	cc -D_REENTRANT thread4.c -o thread4 -lpthread

thread7: thread7.c
	cc -D_REENTRANT thread7.c -o thread7 -lpthread

thread8: thread8.c
	cc -D_REENTRANT thread8.c -o thread8 -lpthread

looptime: looptime.c
	cc -D_REENTRANT looptime.c -o looptime -lpthread

nolooptime: nolooptime.c
	cc -D_REENTRANT nolooptime.c -o nolooptime -lpthread
