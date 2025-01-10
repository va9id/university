/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import com.sysc3303.project.*;
import com.sysc3303.project.elevator.Elevator;
import com.sysc3303.project.elevator.ElevatorEvent;
import com.sysc3303.project.elevator.ElevatorState;
import com.sysc3303.project.elevator.ElevatorEvent.Direction;
import com.sysc3303.project.elevator.ElevatorEvent.Fault;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author Group 9
 *
 */
class ElevatorTest {
	private static Elevator elevator;
	private static final boolean SHOULD_SLEEP = false;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		elevator = new Elevator(SHOULD_SLEEP);
	}
	
	@AfterAll
	public static void tearDown() {
		elevator.closeSocket();
	}


	@Test
	public void testSetStates() {
		assertEquals(ElevatorStateTest.STOPPED_STRING, elevator.getState().toString());

		elevator.setState(new ElevatorState(ElevatorEvent.Direction.UP.name()));
		assertEquals(ElevatorStateTest.UP_STRING, elevator.getState().toString());

		elevator.setState(new ElevatorState(ElevatorEvent.Direction.DOWN.name()));
		assertEquals(ElevatorStateTest.DOWN_STRING, elevator.getState().toString());

		elevator.setState(new ElevatorState(ElevatorEvent.Direction.STOPPED.name()));
		assertEquals(ElevatorStateTest.STOPPED_STRING, elevator.getState().toString());
	}
}
