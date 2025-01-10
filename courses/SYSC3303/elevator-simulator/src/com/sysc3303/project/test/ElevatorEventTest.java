/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sysc3303.project.elevator.ElevatorEvent;
import com.sysc3303.project.elevator.ElevatorEvent.Fault;
import com.sysc3303.project.utils.Time;

/**
 * @author Group 9
 *
 */
class ElevatorEventTest {
	private static ElevatorEvent elevatorEvent;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		elevatorEvent = new ElevatorEvent(new Time("1", "1", "1", "1"), 3, ElevatorEvent.Direction.UP, 4, Fault.NO_FAULT);
	}

	@Test
	void gettersTest() {
		assertEquals(3, elevatorEvent.getFloorNumber());
		assertEquals(ElevatorEvent.Direction.UP, elevatorEvent.getDirection());
		assertEquals(4, elevatorEvent.getCarButton());
	}
	
	/**
	 * Tested extensively in the TimeTest class
	 */
	@Test
	void eventOccursBeforeTest() {
		ElevatorEvent test = new ElevatorEvent(new Time("1", "1", "1", "2"), 3, ElevatorEvent.Direction.UP, 4, Fault.NO_FAULT);
		assertEquals(true, elevatorEvent.eventOccursBefore(test));
	}
	
	@Test
	void toStringTest() {
		assertEquals("ElevatorEvent[Time: 1:1:1.1, Floor: 3, Floor Button: UP, Car Button: 4, NO_FAULT]", elevatorEvent.toString());
	}

}
