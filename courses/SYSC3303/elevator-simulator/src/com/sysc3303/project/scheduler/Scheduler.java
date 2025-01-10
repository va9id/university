/**
 * 
 */
package com.sysc3303.project.scheduler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.sysc3303.project.elevator.ElevatorRequest;
import com.sysc3303.project.elevator.ElevatorResponse;
import com.sysc3303.project.elevator.ElevatorEvent.Direction;
import com.sysc3303.project.floor.Floor;
import com.sysc3303.project.floor.FloorRequest;
import com.sysc3303.project.utils.UDPUtil;

/**
 * The Scheduler class which schedules the elevators and enables communication
 * between the Floor and Elevator subsystems
 * 
 * @author Group 9
 *
 */
public class Scheduler implements Runnable {
	private final Queue<FloorRequest> events = new ArrayDeque<>(); //request queue
	private final List<DatagramPacket> elevatorRequestPackets = new ArrayList<>();
	private final Queue<ElevatorResponse> responses = new ArrayDeque<>(); // response queue
	private SchedulerState state = new SchedulerReceivingState(); //the state of the scheduler
	public static final int FLOOR_REQUEST_PORT = 4999; //Port for floor subsystem to send requests to
	public static final int ELEVATOR_REQUEST_PORT = 5555; //Port for elevators to send requests to
	public static final int ELEVATOR_RESPONSE_PORT = 5556; //Port for elevator to send response objects to
	public static final InetAddress ADDRESS = UDPUtil.getLocalHost();
	public final DatagramSocket responseSocket, elevatorRequestSocket, floorRequestSocket;

	/**
	 * Constructor; initializes all attributes with default values
	 */
	public Scheduler() {
		elevatorRequestSocket = UDPUtil.createDatagramSocket(ELEVATOR_REQUEST_PORT);
		responseSocket = UDPUtil.createDatagramSocket(ELEVATOR_RESPONSE_PORT);
		floorRequestSocket = UDPUtil.createDatagramSocket(FLOOR_REQUEST_PORT);
	}
	
	/**
	 * Closes the sockets associated with the object
	 */
	public void closeSockets() {
		responseSocket.close();
		elevatorRequestSocket.close();
		floorRequestSocket.close();
	}

	/**
	 * Scheduler thread's run method
	 */
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + ": scheduler currently in state "  + state.toString());
		
		Thread requestsFromFloor = new Thread(() -> {
			while (true) {
				receiveRequestFromFloor();
			}
		}, "SchedulerThread-1");
		
		Thread requestsFromElevator = new Thread(() -> {
			while (true) {
				receiveRequestFromElevator();
			}
		}, "SchedulerThread-2");
		
		Thread sendToElevator = new Thread(() -> {
			while (true) {
				sendRequestToElevator();
			}
		}, "SchedulerThread-3");
		
		Thread responsesFromElevator = new Thread(() -> {
			while (true) {
				receiveResponseFromElevator();
			}
		}, "SchedulerThread-4");
		
		Thread sendToFloor = new Thread(() -> {
			while (true) {
				sendResponseToFloor();
			}
		}, "SchedulerThread-5");
		//start scheduler threads
		requestsFromFloor.start();
		requestsFromElevator.start();
		sendToElevator.start();
		responsesFromElevator.start();
		sendToFloor.start();
	}

	/**
	 * Receives FloorRequest from Floor subsystem and adds it to the event queue
	 */
	private void receiveRequestFromFloor() {
		DatagramPacket receivePacket = new DatagramPacket(new byte[UDPUtil.RECEIVE_PACKET_LENGTH], UDPUtil.RECEIVE_PACKET_LENGTH);
		UDPUtil.receivePacket(floorRequestSocket, receivePacket);
		FloorRequest request = (FloorRequest) UDPUtil.convertFromBytes(receivePacket.getData(), receivePacket.getLength());
		
		if (request.isEndOfRequests()) {
			shutDownScheduler(request); //shutdown scheduler subsystem if end of requests
		}
		
		addFloorRequest(request);
		sendAcknowledgmentPacket(receivePacket.getAddress(), receivePacket.getPort(), true);
	}
	
	/**
	 * Sends the response message to the Floor subsystem
	 */
	private void sendResponseToFloor() {
		ElevatorResponse response = getElevatorResponse();
		DatagramSocket socket = UDPUtil.createDatagramSocket();
		byte[] data = response.getMessage().getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, Floor.ADDRESS, Floor.PORT);
		UDPUtil.sendPacket(socket, packet);
		socket.close();
		state.handleResponseProcessed(this); // response has been received and processed
	}
	
	/**
	 * Receives a response object from an elevator and adds it to the response queue
	 */
	public void receiveResponseFromElevator() {
		DatagramPacket receivePacket = new DatagramPacket(new byte[UDPUtil.RECEIVE_PACKET_LENGTH], UDPUtil.RECEIVE_PACKET_LENGTH);
		UDPUtil.receivePacket(responseSocket, receivePacket);
		ElevatorResponse response = (ElevatorResponse) UDPUtil.convertFromBytes(receivePacket.getData(), receivePacket.getLength());
		addElevatorResponse(response);
		sendAcknowledgmentPacket(receivePacket.getAddress(), receivePacket.getPort(), false);
	}
	
	/**
	 * Receives a request from an elevator 
	 */
	public void receiveRequestFromElevator() {
		DatagramPacket receivePacket = new DatagramPacket(new byte[UDPUtil.RECEIVE_PACKET_LENGTH], UDPUtil.RECEIVE_PACKET_LENGTH);
		UDPUtil.receivePacket(elevatorRequestSocket, receivePacket);
		ElevatorRequest elevatorRequest = (ElevatorRequest) UDPUtil.convertFromBytes(receivePacket.getData(), receivePacket.getLength());
		
		if (elevatorRequest.getDirection() == Direction.STOPPED) {
			addElevatorRequestPacket(receivePacket);
		}
		else {
			checkAndSendRequestsOnFloor(receivePacket);
		}
	}
	
	/**
	 * Checks for requests which an elevator can take and sends them. 
	 * 
	 * @param The receivePacket 
	 */
	private synchronized void checkAndSendRequestsOnFloor(DatagramPacket receivePacket) {
		ElevatorRequest elevatorRequest = (ElevatorRequest) UDPUtil.convertFromBytes(receivePacket.getData(), receivePacket.getLength());
		int floor = elevatorRequest.getFloor();
		Direction direction = elevatorRequest.getDirection();
		
		Set<FloorRequest> eventsSet = new HashSet<>();
		
		for (FloorRequest f: new HashSet<>(events)) {
			if (f.getElevatorEvent().getDirection() == direction && f.getElevatorEvent().getFloorNumber() == floor) {
				eventsSet.add(f);
				events.remove(f);
			}
		}
		
		byte[] data =  UDPUtil.convertToBytes(eventsSet);
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, receivePacket.getAddress(), receivePacket.getPort());
		DatagramSocket socket = UDPUtil.createDatagramSocket();
		UDPUtil.sendPacket(socket, sendPacket);
		socket.close();
	}

	/**
	 * Sends a floor request to an elevator
	 */
	private void sendRequestToElevator() {
		Set<FloorRequest> requests = getNextRequest();
		sendRequestToElevator(requests);
	}
	
	/**
	 * Sends a floor request to an elevator.
	 * 
	 * @param The Set requests to send.
	 */
	private void sendRequestToElevator(Set<FloorRequest> requests) {
		byte[] data = UDPUtil.convertToBytes(requests);
		DatagramPacket requestPacket = getClosestElevator(requests.iterator().next().getElevatorEvent().getFloorNumber());
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, requestPacket.getAddress(), requestPacket.getPort());
		DatagramSocket socket = UDPUtil.createDatagramSocket();
		UDPUtil.sendPacket(socket, sendPacket);
		socket.close();
	}
	
	/**
	 * Creates an acknowledgement DatagramPacket and sends it to the specified socket
	 * 
	 * @param address InetAddress to send acknowledgement packet to
	 * @param port	Int port number to send acknowledgement packet to
	 * @param toFloor	boolean true if sending acknowledgement to Floor, else to Elevator
	 */
	private void sendAcknowledgmentPacket(InetAddress address, int port, boolean toFloor) {
		String destination = (toFloor) ? "Floor" : "Elevator";		
		System.out.println(Thread.currentThread().getName() + ": Sending acknowledgment packet to " + destination);
		String message = "REQUEST_ACKNOWLEDGED"; 
		byte[] data = message.getBytes();  
		DatagramPacket acknowledgmentPacket = new DatagramPacket(data, data.length, address, port);
		DatagramSocket socket = UDPUtil.createDatagramSocket(); 
		UDPUtil.sendPacket(socket, acknowledgmentPacket);
		socket.close();
	}
	
	/**
	 * Gets the state of the Scheduler
	 * 
	 * @return the state of the Scheduler
	 */
	public synchronized SchedulerState getState() {
		return state;
	}
	
	/**
	 * Sets the state of the Scheduler
	 * 
	 * @param state the new state of the Scheduler
	 */
	public synchronized void setState(SchedulerState state) {
		this.state = state;
	}

	/**
	 * Adds a FloorRequest to the request and direction queue.
	 * 
	 * @param floorRequest The FloorRequest to add
	 */
	public synchronized void addFloorRequest(FloorRequest floorRequest) {
		if (floorRequest == null) {
			throw new IllegalArgumentException("The FloorRequest object cannot be null");
		}
		events.add(floorRequest);
		
		notifyAll(); // notifies threads that a floor request is available
	}
	
	/**
	 * Gets and removes the FloorRequest at the head of the queue as well as any
	 * other FloorRequest objects on the same floor and going in the same direction
	 * 
	 * @return The Set of FloorRequests
	 */
	private synchronized Set<FloorRequest> getNextRequest() {
		while (events.isEmpty() || elevatorRequestPackets.isEmpty()) {
			try {
				wait(); // wait until a request is available
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		Set<FloorRequest> eventsSet = new HashSet<>();
		FloorRequest removedRequest = events.remove();
		eventsSet.add(removedRequest);
		
		for (FloorRequest f: new HashSet<>(events)) {
			if (f.getElevatorEvent().getDirection() == removedRequest.getElevatorEvent().getDirection() && f.getElevatorEvent().getFloorNumber() == removedRequest.getElevatorEvent().getFloorNumber()) {
				eventsSet.add(f);
				events.remove(f);
			}
		}
		
		return eventsSet;
	}
	
	/**
	 * @return The FloorRequest at the head of the queue
	 */
	public synchronized FloorRequest peekRequestAtFront() {
		return events.peek();
	}
	
	/**
	 * Gets the closest Elevator to the floorRequest
	 * 
	 * @param floorNum The floor number from the Floor Request
	 * @return DatagramPacket from closest Elevator
	 */
	public synchronized DatagramPacket getClosestElevator(int floorNum) {
		while (elevatorRequestPackets.isEmpty()) {
			try {
				wait(); // wait for request from elevator
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		int index = 0;
		DatagramPacket closestPacket = elevatorRequestPackets.get(index);
		ElevatorRequest elevatorRequest = (ElevatorRequest) UDPUtil.convertFromBytes(closestPacket.getData(), closestPacket.getLength());
		int elevatorFloorNum = elevatorRequest.getFloor();
		int closest = Math.abs(floorNum - elevatorFloorNum);
		
		for (int i = 1; i < elevatorRequestPackets.size(); i++) {
			DatagramPacket packet = elevatorRequestPackets.get(i);
			elevatorFloorNum = ((ElevatorRequest) UDPUtil.convertFromBytes(packet.getData(), packet.getLength())).getFloor();
			int difference = Math.abs(elevatorFloorNum - floorNum);
			
			if (difference < closest) {
				closest = difference;
				index = i;
			}
		}
		
		return elevatorRequestPackets.remove(index);	
	}

	/**
	 * Adds an ElevatorResponse to the response queue
	 * 
	 * @param response The ElevatorResponse to add
	 */
	public synchronized void addElevatorResponse(ElevatorResponse response) {
		if (response == null) {
			throw new IllegalArgumentException("The ElevatorResponse object cannot be null");
		}
		responses.add(response);
		notifyAll(); // notifies scheduler that response can be sent to Floor thread
		state.handleResponseReceived(this); //handle response received event
	}

	/**
	 * Gets and removes the ElevatorResponse from the head of the response queue
	 * 
	 * @return elevatorResponse The next ElevatorResponse object
	 */
	public synchronized ElevatorResponse getElevatorResponse() {
		while (responses.isEmpty()) {
			try {
				wait(); // waits for response to be available
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return responses.remove();
	}
	
	/**
	 * @return true if there is at least one response in the response queue, and false otherwise
	 */
	public synchronized boolean isResponseInQueue() {
		return !responses.isEmpty();
	}
	
	/**
	 * Gets the ElevatorRequestPacket ArrayList (Used for testing)
	 */
	public List<DatagramPacket> getElevatorRequestPackets() {
		return elevatorRequestPackets;
	}
	
	/**
	 * Adds an ElevatorRequest packet to the elevatorRequestPackets ArrayList
	 * 
	 * @param packet The ElevatorRequest packet to add
	 */
	public synchronized void addElevatorRequestPacket(DatagramPacket packet) {
		if (packet == null) {
			throw new IllegalArgumentException("The DatagramPacket cannot be null");
		}
		
		elevatorRequestPackets.add(packet);
		notifyAll();
	}

	/**
	 * Shuts down the Scheduler subsystem and forwards on the request to the Elevator subsystem
	 * 
	 * @param request The request to be forwarded on to the Elevator subsystem
	 */
	private void shutDownScheduler(FloorRequest request) {
		// Send message to elevator to shut down
		sendRequestToElevator(new HashSet<>(Arrays.asList(request)));
		System.out.println("Scheduler notified Elevator subsystem of end of requests, shutting down");

		closeSockets();
		System.exit(0);
	}

	/**
	 * Entry point to Scheduler subsystem
	 */
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		Thread thread = new Thread(scheduler, "SchedulerSubsystem");
		thread.start();
	}

}
