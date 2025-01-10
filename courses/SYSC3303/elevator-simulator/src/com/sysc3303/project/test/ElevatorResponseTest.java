/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sysc3303.project.elevator.ElevatorResponse;
import com.sysc3303.project.floor.Floor;
import com.sysc3303.project.scheduler.Scheduler;

/**
 * @author Group 9
 *
 */
class ElevatorResponseTest {
	private static ElevatorResponse elevatorResponse;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		elevatorResponse = new ElevatorResponse("Testing");
	}

	@Test
	void gettersTest() {
		assertEquals("Testing", elevatorResponse.getMessage());
	}

}
