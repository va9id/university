package com.sysc3303.project.test;

import com.sysc3303.project.*;
import com.sysc3303.project.elevator.Elevator;
import com.sysc3303.project.elevator.ElevatorEvent;
import com.sysc3303.project.elevator.ElevatorState;
import com.sysc3303.project.elevator.ElevatorEvent.Direction;
import com.sysc3303.project.elevator.ElevatorEvent.Fault;
import com.sysc3303.project.floor.Floor;
import com.sysc3303.project.floor.FloorRequest;
import com.sysc3303.project.scheduler.Scheduler;
import com.sysc3303.project.utils.Time;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

class ElevatorStateTest {

    private static ElevatorState elevatorState;
    private static Elevator elevator;
    private static final String STATE_STRING = " STATE";
    public static final String STOPPED_STRING = ElevatorEvent.Direction.STOPPED.toString() + STATE_STRING;
    public static final String UP_STRING = ElevatorEvent.Direction.UP.toString() + STATE_STRING;
    public static final String DOWN_STRING = ElevatorEvent.Direction.DOWN.toString() + STATE_STRING;
    private static final boolean SHOULD_SLEEP = false; //do not sleep in test cases

    @BeforeEach
    public void setUpBeforeClass() {
        elevator = new Elevator(SHOULD_SLEEP);
        elevatorState = elevator.getState();
    }
    
    @AfterEach
    public void tearDown() {
    	elevator.closeSocket();
    }

    @Test
    public void testInitialState() {
        ElevatorState es = new ElevatorState(ElevatorEvent.Direction.STOPPED.toString(), SHOULD_SLEEP);
        assertEquals(STOPPED_STRING, es.toString());

        es = new ElevatorState(ElevatorEvent.Direction.UP.toString(), SHOULD_SLEEP);
        assertEquals(UP_STRING, es.toString());

        es = new ElevatorState(ElevatorEvent.Direction.DOWN.toString(), SHOULD_SLEEP);
        assertEquals(DOWN_STRING, es.toString());
    }

    @Test
    public void testHandleRequest() {
    	Scheduler scheduler = new Scheduler(); //need a scheduler for this test to respond to elevator requests
    	
    	//Test inputs
    	final int TEST1_FROM = Floor.BOTTOM_FLOOR;
    	final int TEST1_TO = 5;
    	final int TEST2_FROM = 5;
    	final int TEST2_TO = 3;
    	final int TEST3_FROM = 3;
    	final int TEST3_TO = 4;
    	final int TEST4_FROM = 4;
    	final int TEST4_TO = 1;
    	final int NUM_TESTS = 4;
    	
    	//Need a thread for the scheduler to receive requests
    	Thread schedulerTestThread1 = new Thread(() -> {
    		int numLoops = Math.abs(TEST1_FROM - TEST1_TO) + Math.abs(TEST2_FROM - TEST2_TO) + Math.abs(TEST3_FROM - TEST3_TO) + Math.abs(TEST4_FROM - TEST4_TO);
    		for (int i=0; i < numLoops; i++) {
    			scheduler.receiveRequestFromElevator();
    		}
    	});
    	
    	//Need a thread for the scheduler to send acknowledgements
    	Thread schedulerTestThread2 = new Thread(() -> {
    		int numLoops = NUM_TESTS;
    		for (int i=0; i < numLoops; i++) {
    			scheduler.receiveResponseFromElevator();
    		}
    	});
    	
		schedulerTestThread1.start();
		schedulerTestThread2.start();
    	
        // test moving from floor 1 to 5
    	Set<FloorRequest> test = new HashSet<>(Arrays.asList(new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), TEST1_FROM, Direction.UP, TEST1_TO, Fault.NO_FAULT)))); 
        elevatorState.handleRequest(elevator, test);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
        assertEquals(5, elevator.getCurrentFloor());
       
        // test moving from floor 5 to 3
    	Set<FloorRequest> test2 = new HashSet<>(Arrays.asList(new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), TEST2_FROM, Direction.DOWN, TEST2_TO, Fault.NO_FAULT)))); 
        elevatorState.handleRequest(elevator, test2);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
        assertEquals(3, elevator.getCurrentFloor());

        // test moving from floor 3 to 4
    	Set<FloorRequest> test3 = new HashSet<>(Arrays.asList(new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), TEST3_FROM, Direction.UP, TEST3_TO, Fault.NO_FAULT)))); 
        elevatorState.handleRequest(elevator, test3);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
        assertEquals(4, elevator.getCurrentFloor());
        
        // test moving from floor 4 to 1
    	Set<FloorRequest> test4 = new HashSet<>(Arrays.asList(new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), TEST4_FROM, Direction.DOWN, TEST4_TO, Fault.NO_FAULT)))); 
        elevatorState.handleRequest(elevator, test4);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
        assertEquals(1, elevator.getCurrentFloor());
        
        scheduler.closeSockets();
    }
    
    @Test
    public void testGoToFloor() {
        //test going up
    	elevatorState.goToFloor(elevator, Direction.UP, 3);
        assertEquals(3, elevator.getCurrentFloor());
        
        //test going down
        elevatorState.goToFloor(elevator, Direction.DOWN, 1);
        assertEquals(1, elevator.getCurrentFloor());
    }

    @Test
    public void testHandleReachedDestination() {
        // Test to make sure elevator state moves into "stopped state"
        elevatorState.goToFloor(elevator, Direction.UP, 2);
        elevatorState.handleReachedDestination(elevator, 2, false);
        assertEquals(STOPPED_STRING, elevator.getState().toString());
    }
    
    @Test
    public void testHandleFaults() {
		Set<ElevatorEvent> events = new HashSet<ElevatorEvent>();
		ElevatorEvent ee1 = new ElevatorEvent(new Time("1", "1", "1", "1"), 1, Direction.UP, 2, Fault.NO_FAULT);

		ElevatorEvent ee2 = new ElevatorEvent(new Time("2", "2", "2", "2"), 4, Direction.DOWN, 3, Fault.HARD_FAULT);

		ElevatorEvent ee3 = new ElevatorEvent(new Time("1", "1", "1", "1"), 1, Direction.UP, 2, Fault.TRANSIENT_FAULT);

		events.add(ee1);
		assertEquals(false, elevatorState.handleFaults(elevator, events));

		events.clear();
		events.add(ee2);

		// Need a thread for the scheduler to send acknowledgement
		Scheduler scheduler = new Scheduler();
		Thread schedulerThread = new Thread(() -> {
			scheduler.receiveResponseFromElevator();
		});
		schedulerThread.start();

		assertEquals(true, elevatorState.handleFaults(elevator, events));
		scheduler.closeSockets();

		events.clear();
		events.add(ee3);
		assertEquals(false, elevatorState.handleFaults(elevator, events));
    }
}