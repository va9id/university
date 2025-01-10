/**
 * 
 */
package com.sysc3303.project.test;

import static org.junit.jupiter.api.Assertions.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Test;

import com.sysc3303.project.elevator.ElevatorEvent;
import com.sysc3303.project.elevator.ElevatorEvent.Direction;
import com.sysc3303.project.elevator.ElevatorEvent.Fault;
import com.sysc3303.project.floor.FloorRequest;
import com.sysc3303.project.utils.Time;
import com.sysc3303.project.utils.UDPUtil;

/**
 * @author Group 9
 */
public class UDPUtilTest {
	
	@Test
	public void testCreateSocket() {
		DatagramSocket udpUtilSocket = UDPUtil.createDatagramSocket();
		assertTrue(udpUtilSocket instanceof DatagramSocket);
		assertNotNull(udpUtilSocket);
		udpUtilSocket.close();
	}
	
	@Test
	public void testCreateSocketWithPort() {
		int testPort = 69;
		DatagramSocket udpUtilSocket = UDPUtil.createDatagramSocket(testPort);
		
		assertTrue(udpUtilSocket instanceof DatagramSocket);
		assertNotNull(udpUtilSocket);
		assertEquals(testPort, udpUtilSocket.getLocalPort());
		
		udpUtilSocket.close();
	}
	
	/**
	 * @throws UnknownHostException
	 */
	@Test
	public void testGetLocalHost() throws UnknownHostException {
		assertEquals(InetAddress.getLocalHost(), UDPUtil.getLocalHost());
	}
	
	@Test 
	public void testConvertToBytes() {
		ElevatorEvent event = new ElevatorEvent(new Time("14", "05", "15", "000"), 1, Direction.UP, 3, Fault.NO_FAULT);
		FloorRequest testObject = new FloorRequest(event);
		assertNotNull(UDPUtil.convertToBytes(testObject));
	}
	
	@Test
	public void testConvertFromBytes() {
		ElevatorEvent event = new ElevatorEvent(new Time("14", "05", "15", "000"), 1, Direction.UP, 3, Fault.NO_FAULT);
		FloorRequest testObject = new FloorRequest(event);
		byte[] data =  UDPUtil.convertToBytes(testObject);
		
		assertNotNull(UDPUtil.convertFromBytes(data));
		assertTrue(UDPUtil.convertFromBytes(data) instanceof FloorRequest);
		
	}
	
	@Test
	public void testConvertFromBytesWithLength() {
		ElevatorEvent event = new ElevatorEvent(new Time("14", "05", "15", "000"), 1, Direction.UP, 3, Fault.NO_FAULT);
		FloorRequest testObject = new FloorRequest(event);
		byte[] data =  UDPUtil.convertToBytes(testObject);
		
		assertNotNull(UDPUtil.convertFromBytes(data, data.length));
		assertTrue(UDPUtil.convertFromBytes(data, data.length) instanceof FloorRequest);
		
	}
}
