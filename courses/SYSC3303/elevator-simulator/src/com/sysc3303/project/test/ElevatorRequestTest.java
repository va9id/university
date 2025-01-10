/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.sysc3303.project.elevator.ElevatorRequest;
import com.sysc3303.project.elevator.ElevatorEvent.Direction;

/**
 * @author Group 9
 *
 */
class ElevatorRequestTest {

	@Test
	void testCreation() {
		ElevatorRequest request1 = new ElevatorRequest(3, Direction.DOWN);
		ElevatorRequest request2 = new ElevatorRequest(100, Direction.UP);
		ElevatorRequest request3 = new ElevatorRequest(10, Direction.STOPPED);
		assertNotNull(request1);
		assertNotNull(request2);
		assertNotNull(request3);
		
	}
	
	@Test
	void testGetFloor() {
		ElevatorRequest request1 = new ElevatorRequest(3, Direction.DOWN);
		ElevatorRequest request2 = new ElevatorRequest(100, Direction.UP);
		ElevatorRequest request3 = new ElevatorRequest(10, Direction.STOPPED);
		assertEquals(request1.getFloor(), 3);
		assertEquals(request2.getFloor(), 100);
		assertEquals(request3.getFloor(), 10);
	}
	
	@Test
	void testGetDirection() {
		ElevatorRequest request1 = new ElevatorRequest(3, Direction.DOWN);
		ElevatorRequest request2 = new ElevatorRequest(100, Direction.UP);
		ElevatorRequest request3 = new ElevatorRequest(10, Direction.STOPPED);
		assertEquals(request1.getDirection(), Direction.DOWN);
		assertEquals(request2.getDirection(), Direction.UP);
		assertEquals(request3.getDirection(), Direction.STOPPED);
		
	}

}
