package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sysc3303.project.elevator.ElevatorEvent;
import com.sysc3303.project.elevator.ElevatorEvent.Direction;
import com.sysc3303.project.elevator.ElevatorEvent.Fault;
import com.sysc3303.project.floor.Floor;
import com.sysc3303.project.floor.FloorRequest;
import com.sysc3303.project.scheduler.Scheduler;
import com.sysc3303.project.utils.Time;

/**
 * @author Group 9
 *
 */
class FloorRequestTest {
	private static FloorRequest floorRequest;
	private static ElevatorEvent elevatorEvent = new ElevatorEvent(new Time("1", "1", "1", "1"), 3, ElevatorEvent.Direction.UP, 4, Fault.NO_FAULT);

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		floorRequest = new FloorRequest(elevatorEvent);
	}

	@Test
	void gettersTest() {
		assertEquals(elevatorEvent, floorRequest.getElevatorEvent());
	}

}
