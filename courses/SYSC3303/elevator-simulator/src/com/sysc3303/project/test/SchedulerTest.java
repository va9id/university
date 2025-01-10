/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;

import java.net.DatagramPacket;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import com.sysc3303.project.*;
import com.sysc3303.project.elevator.ElevatorEvent;
import com.sysc3303.project.elevator.ElevatorRequest;
import com.sysc3303.project.elevator.ElevatorResponse;
import com.sysc3303.project.elevator.ElevatorEvent.Direction;
import com.sysc3303.project.elevator.ElevatorEvent.Fault;
import com.sysc3303.project.floor.FloorRequest;
import com.sysc3303.project.scheduler.Scheduler;
import com.sysc3303.project.scheduler.SchedulerReceivingSendingState;
import com.sysc3303.project.scheduler.SchedulerReceivingState;
import com.sysc3303.project.utils.Time;
import com.sysc3303.project.utils.UDPUtil;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Group 9
 *
 */
class SchedulerTest {
	
	private static Scheduler scheduler;

	public static final String RECEIVING_STRING = "RECEIVING STATE";
	public static final String RECEIVING_SENDING_STRING = "RECEIVING AND SENDING STATE";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUpBeforeClass() throws Exception {
		scheduler = new Scheduler();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		scheduler.closeSockets();
	}

	@Test
	public void testFloorRequest() {
		FloorRequest floorRequest = new FloorRequest(new ElevatorEvent(new Time("1", "1", "1", "1"), 3, ElevatorEvent.Direction.UP, 4, Fault.NO_FAULT));
		scheduler.addFloorRequest(floorRequest);
		assertEquals(floorRequest, scheduler.peekRequestAtFront());
	}
	
	@Test
	public void testElevatorResponse() {
		ElevatorResponse elevatorResponse = new ElevatorResponse("Done!");
		scheduler.addElevatorResponse(elevatorResponse);
		assertEquals(elevatorResponse, scheduler.getElevatorResponse());
	}

	// Don't need to test with threads like in Elevator test, unit testing scheduler states is equivalent
	@Test
	public void testSetAndDefaultState() {
		assertEquals(scheduler.getState().toString(), RECEIVING_STRING);

		scheduler.setState(new SchedulerReceivingState());
		assertEquals(scheduler.getState().toString(), RECEIVING_STRING);

		scheduler.setState(new SchedulerReceivingSendingState());
		assertEquals(scheduler.getState().toString(), RECEIVING_SENDING_STRING);

		scheduler.setState(new SchedulerReceivingSendingState());
		assertEquals(scheduler.getState().toString(), RECEIVING_SENDING_STRING);
		
		scheduler.setState(new SchedulerReceivingState()); //return to initial state
	}
	
	@Test 
	public void testAddElevatorRequestPacket() {
		addElevatorRequestPackets();
		assertNotNull(scheduler.getElevatorRequestPackets());
		assertEquals(3, scheduler.getElevatorRequestPackets().size());
	}
	
	@Test 
	public void testGetClosestElevator() {
		addElevatorRequestPackets();
		DatagramPacket closestPacket = scheduler.getClosestElevator(2);
		int elevatorFloorNum = ((ElevatorRequest) UDPUtil.convertFromBytes(closestPacket.getData(), closestPacket.getLength())).getFloor();
		assertEquals(1, elevatorFloorNum);
		
		DatagramPacket closestPacket2 = scheduler.getClosestElevator(5);
		int elevatorFloorNum2 = ((ElevatorRequest) UDPUtil.convertFromBytes(closestPacket2.getData(), closestPacket2.getLength())).getFloor();
		assertEquals(4, elevatorFloorNum2);
		
		DatagramPacket closestPacket3 = scheduler.getClosestElevator(6);
		int elevatorFloorNum3 = ((ElevatorRequest) UDPUtil.convertFromBytes(closestPacket3.getData(), closestPacket3.getLength())).getFloor();
		assertEquals(7, elevatorFloorNum3);
	}
	
	private void addElevatorRequestPackets() {
		byte[] data = UDPUtil.convertToBytes(new ElevatorRequest(7, Direction.STOPPED));
		DatagramPacket testPacket = new DatagramPacket(data, data.length, Scheduler.ADDRESS, Scheduler.ELEVATOR_REQUEST_PORT);
		
		byte[] data2 = UDPUtil.convertToBytes(new ElevatorRequest(1, Direction.STOPPED));
		DatagramPacket testPacket2 = new DatagramPacket(data2, data2.length, Scheduler.ADDRESS, Scheduler.ELEVATOR_REQUEST_PORT);
		
		byte[] data3 = UDPUtil.convertToBytes(new ElevatorRequest(4, Direction.STOPPED));
		DatagramPacket testPacket3 = new DatagramPacket(data3, data3.length, Scheduler.ADDRESS, Scheduler.ELEVATOR_REQUEST_PORT);

		scheduler.addElevatorRequestPacket(testPacket);
		scheduler.addElevatorRequestPacket(testPacket2);
		scheduler.addElevatorRequestPacket(testPacket3);
	}
}
