/**
 * 
 */
package com.sysc3303.project.elevator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import com.sysc3303.project.elevator.ElevatorEvent.Direction;
import com.sysc3303.project.floor.Floor;
import com.sysc3303.project.floor.FloorRequest;
import com.sysc3303.project.gui.ElevatorPanel;
import com.sysc3303.project.gui.GUI;
import com.sysc3303.project.scheduler.Scheduler;
import com.sysc3303.project.utils.UDPUtil;

/**
 * Corresponds to elevators in the Elevator subsystem.
 * 
 * @author Group 9
 */
public class Elevator implements Runnable {
	private ElevatorState state;
	private int currentFloor = Floor.BOTTOM_FLOOR;
	public static final int NUM_CARS = 4;
	private final DatagramSocket socket;
	public static final ArrayList<Elevator> elevators = new ArrayList<>();
	private boolean operational = true;
	private ElevatorPanel guiElevatorPanel; 

	/**
	 * Constructor for Elevator object.
	 */
	public Elevator() {
		this(true);
	}
	
	/**
	 * Constructor for Elevator that stops thread from sleeping.
	 * 
	 * @param shouldSleep a flag which indicates if the thread should sleep between events or not
	 */
	public Elevator(boolean shouldSleep) {
		socket = UDPUtil.createDatagramSocket();
		state = new ElevatorState(ElevatorEvent.Direction.STOPPED.toString(), shouldSleep);
		guiElevatorPanel = new ElevatorPanel(); 
		
	}
	
	/**
	 * Get elevator gui panel 
	 * 
	 * @return ElevatorPanel
	 */
	public ElevatorPanel getElevatorPanel() {
		return guiElevatorPanel; 
	}
	
	
	/**
	 * @return the current state of the elevator
	 */
	public ElevatorState getState() {
		return this.state;
	}
	
	/**
	 * @param state the state to set the elevator to
	 */
	public void setState(ElevatorState state) {
		this.state = state;
	}
	
	/**
	 * @return the current floor of the elevator
	 */
	public int getCurrentFloor() {
		return this.currentFloor;
	}
	
	/**
	 * @param floor the floor to set the elevator to
	 */
	public void setCurrentFloor(int floor) {
		this.currentFloor = floor;
	}
	
	/**
	 * Sets if the elevator is operational or not
	 * 
	 * @param isOperational true if the elevator is operational, and false otherwise
	 */
	public void setOperational(boolean isOperational) {
		operational = isOperational;
	}
	
	/**
	 * Closes the socket associated with the elevator object
	 */
	public void closeSocket() {
		socket.close();
	}

	/**
	 * Elevators thread run method.
	 */
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + ": elevator starting on floor " + currentFloor + " in state " + state.toString());
		while (operational) {
			Set<FloorRequest> requests = getNextFloorRequest(); // gets next request from scheduler to process
			FloorRequest request = requests.iterator().next();
			if (requests.size() == 1 && request.isEndOfRequests()) {
				exitElevatorSubsystem();
			}
			boolean isHardFault =  processElevatorEvents(requests);
			if (!isHardFault) {
				state.handleRequest(this, requests);
			}
		}
	}

	/**
	 * @return the Set of FloorRequest objects sent from the scheduler
	 */
	private Set<FloorRequest> getNextFloorRequest() {
		DatagramPacket receivePacket = sendAndReceiveRequest(currentFloor, Direction.STOPPED);
		return (Set<FloorRequest>) UDPUtil.convertFromBytes(receivePacket.getData(), receivePacket.getLength());
	}

	/**
	 * Sends an ElevatorRequest and receives a set of FloorRequests from Scheduler.
	 * 
	 * @param floorNum The floor number of the elevator.
	 * @param direction The direction of the elevator.
	 * @return The receive packet sent by the scheduler.
	 */
	public DatagramPacket sendAndReceiveRequest(int floorNum, Direction direction) {
		byte[] data = UDPUtil.convertToBytes(new ElevatorRequest(floorNum, direction));
		DatagramPacket packet = new DatagramPacket(data, data.length, Scheduler.ADDRESS, Scheduler.ELEVATOR_REQUEST_PORT);
		UDPUtil.sendPacket(socket, packet);
		DatagramPacket receivePacket = new DatagramPacket(new byte[UDPUtil.RECEIVE_PACKET_LENGTH], UDPUtil.RECEIVE_PACKET_LENGTH);
		UDPUtil.receivePacket(socket, receivePacket);
		return receivePacket;
	}

	/**
	 * Helper method to process an ElevatorEvent from the scheduler
	 * 
	 * @param events The Set of events currently being processed
	 */
	private boolean processElevatorEvents(Set<FloorRequest> requests) {

		printReceivedRequests(requests);
		
		Set<ElevatorEvent> events = convertToElevatorEventSet(requests);
		if (checkAndHandleFault(events)) {
			return true;
		}
		
		int eventFloorNumber = requests.iterator().next().getElevatorEvent().getFloorNumber();
		
		//Move to the appropriate floor if the elevator is not already there
		if (currentFloor != eventFloorNumber) {
			ElevatorEvent.Direction direction = currentFloor < eventFloorNumber ? ElevatorEvent.Direction.UP : ElevatorEvent.Direction.DOWN;
			guiElevatorPanel.addStar(eventFloorNumber);
			state.goToFloor(this, direction, eventFloorNumber);
		}
		
		printPeopleEntered(requests);
		
		return false;
	}
	
	/**
	 * Prints out the new requests received by the elevator
	 * 
	 * @param requests The new requests received by the elevator
	 */
	public void printReceivedRequests(Set<FloorRequest> requests) {
		for (FloorRequest f: requests) {
			System.out.println(Thread.currentThread().getName() + ": received " + f.getElevatorEvent().toString());
		}
	}
	
	/**
	 * Prints that people have entered the elevator and the buttons they have pressed
	 * 
	 * @param requests The requests corresponding to the people entering
	 */
	public void printPeopleEntered(Set<FloorRequest> requests) {
		System.out.println(Thread.currentThread().getName() + ": people have entered into the elevator");
		
		for (FloorRequest f: requests) {
			System.out.println(Thread.currentThread().getName() + ": Button " + f.getElevatorEvent().getCarButton() + " Light is ON");
			guiElevatorPanel.highlightDestination(f.getElevatorEvent().getCarButton());
		}
	}
	

	/**
	 * Helper method to set a response for the scheduler
	 * 
	 * @param events The Set of ElevatorEvents to response with
	 * @param isHardFault The boolean used to distinguish if hard fault occured. 
	 */
	public void setResponseForScheduler(Set<ElevatorEvent> events, boolean isHardFault) {
		String message = (isHardFault) ? " has failed due to a hard fault" : " has been processed successfully";
		StringBuilder sb = new StringBuilder();
		for (ElevatorEvent e: events) {
			sb.append(e.toString() + message).append("\n");
		}
		sb.setLength(sb.length() - 1);
		String responseMessage = sb.toString();
		ElevatorResponse response = new ElevatorResponse(responseMessage);
		sendElevatorResponse(response);
	}
	
	/**
	 * Adds ElevatorResponse to scheduler's response queue
	 * 
	 * @param response the response object to send to the scheduler
	 */
	private void sendElevatorResponse(ElevatorResponse response) {
		byte[] data = UDPUtil.convertToBytes(response);
		DatagramPacket packet = new DatagramPacket(data, data.length, Scheduler.ADDRESS, Scheduler.ELEVATOR_RESPONSE_PORT);
		UDPUtil.sendPacket(socket, packet);
		System.out.println(Thread.currentThread().getName() + ": sending response to Scheduler");
		receiveAcknowledgment(); //Receives acknowledgment pack from the scheduler
	}
	
	/**
	 * Receives an acknowledgement packet from the scheduler
	 */
	public void receiveAcknowledgment() {
		DatagramPacket acknowledgementPacket = new DatagramPacket(new byte[UDPUtil.RECEIVE_PACKET_LENGTH], UDPUtil.RECEIVE_PACKET_LENGTH);
		UDPUtil.receivePacket(socket, acknowledgementPacket);
		System.out.print(Thread.currentThread().getName() + ": Received acknowledgment packet from Scheduler containing "
				+ new String(acknowledgementPacket.getData(), 0, acknowledgementPacket.getLength()) + "\n");
	}
	
	/**
	 * Converts set of FloorRequests to set of ElevatorEvents
	 * 
	 * @param requests The FloorRequests set to convert.
	 * @return a set of ElevatorEvents
	 */
	public Set<ElevatorEvent> convertToElevatorEventSet(Set<FloorRequest> requests) {
		return requests.stream().map(request -> request.getElevatorEvent()).collect(Collectors.toSet());
	}
	
	/**
	 * Checks the set of ElevatorEvents for a fault and handles it.
	 * 
	 * @param events The set of ElevatorEvents.
	 * @return True if hard fault, otherwise false.
	 */
	private boolean checkAndHandleFault(Set<ElevatorEvent> events) {
		return state.handleFaults(this, events);
	}
	

	/**
	 * Closes the socket for each elevator so they are not waiting to receive from the scheduler after
	 * an elevator has already received the final message from the scheduler
	 */
	private static void closeElevatorSockets() {
		for (Elevator elevator : elevators) {
			elevator.closeSocket();
		}
	}
	
	/**
	 * exits the ElevatorSubsystem 
	 */
	private void exitElevatorSubsystem() {
		closeElevatorSockets();
		System.exit(0);
	}
	
	/**
	 * Main method for the Elevator Subsystem
	 */
	public static void main(String[] args) {
		for (int i=0; i<NUM_CARS; i++) {
			Elevator e = new Elevator();
			elevators.add(e);
			Thread t = new Thread(e, "ElevatorThread-" + (i + 1));
			t.start();
		}
		new GUI(); 
	}

}
