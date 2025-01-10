package com.sysc3303.project.test;

import com.sysc3303.project.scheduler.Scheduler;
import com.sysc3303.project.scheduler.SchedulerReceivingSendingState;
import com.sysc3303.project.scheduler.SchedulerReceivingState;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;

public class SchedulerReceivingSendingStateTest {

    private static Scheduler scheduler;
    private static SchedulerReceivingSendingState receivingSendingState;

    /**
     * @throws java.lang.Exception
     */
    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        scheduler = new Scheduler();
        receivingSendingState = new SchedulerReceivingSendingState();
    }
    
    @AfterAll
    public static void tearDown() {
    	scheduler.closeSockets();
    }

    @Test
    public void testHandleResponseProcessed() {
        scheduler.setState(new SchedulerReceivingSendingState());
        // The scheduler shouldn't have anything in the queue so this will change the state
        receivingSendingState.handleResponseProcessed(scheduler);
        assertEquals(SchedulerTest.RECEIVING_STRING, scheduler.getState().toString());
    }
}
