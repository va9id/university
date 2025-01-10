/**
 * 
 */
package com.sysc3303.project.elevator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;
import java.util.Set;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import com.sysc3303.project.elevator.ElevatorEvent.Direction;
import com.sysc3303.project.elevator.ElevatorEvent.Fault;
import com.sysc3303.project.floor.FloorRequest;
import com.sysc3303.project.utils.UDPUtil;

/**
 * Class to represent the elevator's state
 * 
 * @author Group 9
 *
 */
public class ElevatorState {
	private boolean shouldSleep = true; // sets whether the elevator should sleep between events; by default set to true
	private String stateName;
	private final int TIME_REACH_FLOOR_BEFORE_DOORS_OPEN = 1000;
	private final int TIME_DOORS_OPEN = 3000;
	private final int TIME_BETWEEN_FLOORS = 2000;
	private final int TRANS_FAULT_SLEEP_TIME = 5000;
	public static final String REPAIRING_STATE_NAME = "REPAIRING";

	/**
	 * Constructor; initializes ElevatorState with the state name.
	 * 
	 * @param stateName the name of the state
	 */
	public ElevatorState(String stateName) {
		this(stateName, true);
	}

	/**
	 * Constructor; initializes ElevatorState with the state name and the
	 * shouldSleep flag
	 * 
	 * @param stateName   the name of the state
	 * @param shouldSleep the flag which indicated whether the calling thread should
	 *                    sleep when using this object
	 */
	public ElevatorState(String stateName, boolean shouldSleep) {
		this.stateName = stateName;
		this.shouldSleep = shouldSleep;
	}
	
	/**
	 * @param elevator The elevator whose state is being handled
	 * @param events The set events that are being checked for faults
	 * @return true if there is a hard fault in the event set, and false otherwise
	 */
	public boolean handleFaults(Elevator elevator, Set<ElevatorEvent> events) {
		//Check for fault in request set
		for (ElevatorEvent event : events) {
			Fault fault = event.getFault();

			if (fault == Fault.HARD_FAULT) {
				handleHardFault(elevator, events);
				return true; // Need to return so events do not get processed
			}

			else if (fault == Fault.TRANSIENT_FAULT) {
				handleTransientFault(elevator);
			}
		}

		return false;
	}
	
	/**
	 * Handles a hard fault ElevatorEvent (shuts down Elevator).
	 * 
	 * @param events The set of events to send a response.
	 */
	private void handleHardFault(Elevator elevator, Set<ElevatorEvent> events) {
		System.out.println(Thread.currentThread().getName() + ": encountered a hard fault");
		elevator.setResponseForScheduler(events, true);
		elevator.setOperational(false);
		System.out.println(Thread.currentThread().getName() + ": shutting down!");
		elevator.getElevatorPanel().handleHardFault();
	}
	
	/**
	 * Handles transient faults.
	 */
	private void handleTransientFault(Elevator elevator) {
		try {
			System.out.println(Thread.currentThread().getName() + ": elevator encountered a transient fault");
			setNewState(REPAIRING_STATE_NAME, elevator);
			if (shouldSleep) {
				Thread.sleep(TRANS_FAULT_SLEEP_TIME); //Sleep to simulate repairing elevator
			}
			
			System.out.println(Thread.currentThread().getName() + ": elevator resolved it's transient fault");
			elevator.getElevatorPanel().handleTransientFaultFixed();
			setNewState(ElevatorEvent.Direction.STOPPED.toString(), elevator);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Gets the direction of the Elevator.
	 * 
	 * @return The direction emum.
	 */
	public Direction getDirection() {
		return Direction.valueOf(stateName.toUpperCase());
	}

	/**
	 * Handles an up or down request event in the elevator's state machine, and
	 * transitions the elevator into the appropriate state.
	 * 
	 * @param elevator the current elevator
	 * @param requests the Set of FLoorRequests
	 */
	public void handleRequest(Elevator elevator, Set<FloorRequest> requests) {
		closeDoors(elevator);
		Direction direction = requests.iterator().next().getElevatorEvent().getDirection();
		setNewState(direction.toString(), elevator);
		moveBetweenFloors(elevator, requests);
	}

	/**
	 * Moves an elevator to a floor.
	 * 
	 * @param elevator       the elevator object.
	 * @param direction      the direction of the elevator.
	 * @param requestedFloor the requested floor.
	 */
	public void goToFloor(Elevator elevator, ElevatorEvent.Direction direction, int requestedFloor) {
		closeDoors(elevator);
		setNewState(direction.toString(), elevator);
		moveToFloor(Math.abs(requestedFloor - elevator.getCurrentFloor()), elevator);
		handleReachedDestination(elevator, requestedFloor, false);

	}

	/**
	 * Move to a given floor to pick up a person
	 * 
	 * @param numFloors the number of floors that the elevator is moving
	 */
	private void moveToFloor(int numFloors, Elevator elevator) {	
		for (int i = 0; i < numFloors; i++) {
			if (shouldSleep) {
				try {
					Thread.sleep(TIME_BETWEEN_FLOORS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			updateFloor(getDirection(), elevator);
		}
	}
	
	/**
	 * Handle updating elevator floor by incrementing its current floor
	 * and updating the GUI to show the changes.  
	 * 
	 * @param direction	Direction elevator is moving in
	 * @param elevator
	 */
	private void updateFloor(Direction direction, Elevator elevator) {
		if(direction == Direction.UP) {
			elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
			elevator.getElevatorPanel().goingUp();
		} else {
			elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
			elevator.getElevatorPanel().goingDown();
		}
	}

	/**
	 * Sleeps for an amount of time corresponding to the distance the elevator is
	 * moving
	 * 
	 * @param elevator the object
	 * @param requests the FloorRequest Sets
	 */
	private void moveBetweenFloors(Elevator elevator, Set<FloorRequest> requests) {
		Direction direction = requests.iterator().next().getElevatorEvent().getDirection();

		while (!requests.isEmpty()) {
			if (shouldSleep) {
				try {
					Thread.sleep(TIME_BETWEEN_FLOORS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			int nextFloor = direction == Direction.UP ? elevator.getCurrentFloor() + 1 : elevator.getCurrentFloor() - 1;
			updateFloor(direction, elevator);
			Set<ElevatorEvent> events = new HashSet<>();

			DatagramPacket receivePacket = elevator.sendAndReceiveRequest(nextFloor, direction);
			Set<FloorRequest> newRequests = (Set<FloorRequest>) UDPUtil.convertFromBytes(receivePacket.getData(), receivePacket.getLength());
			
			
			for (FloorRequest f : new HashSet<>(requests)) {
				if (f.getElevatorEvent().getCarButton() == nextFloor) {
					events.add(f.getElevatorEvent());
					requests.remove(f);
				}
			}
			
			boolean peopleExitedOnFloor = !events.isEmpty();

			if (peopleExitedOnFloor) {
				handleReachedDestination(elevator, events.iterator().next().getCarButton(), true);
				elevator.setResponseForScheduler(events, false);
			}
			
			if (!newRequests.isEmpty()) { 
				requests.addAll(newRequests); //adds new people entering into elevator
				elevator.printReceivedRequests(newRequests);
				
				//Elevator has to stop at floor if not already stopped
				if(!peopleExitedOnFloor)
					handleReachedDestination(elevator, nextFloor, false);
				
				elevator.printPeopleEntered(newRequests);
				
				Set<ElevatorEvent> newEvents = elevator.convertToElevatorEventSet(newRequests);
				
				if (handleFaults(elevator, newEvents)) {
					return;
				}
			}
			
			//continue moving if there are still more requests to serve
			if (!requests.isEmpty() && this.getDirection() != direction) {
				closeDoors(elevator);
				setNewState(direction.toString(), elevator);
			}
		}
		// need to update GUI panel when no further requests
		elevator.getElevatorPanel().handleFinishedMoving();
	}

	/**
	 * Handles a reach destination event in the elevator's state machine, and
	 * transitions the elevator to the STOPPED state.
	 * 
	 * @param elevator      the current elevator
	 * @param floorNumber   the floor number that the elevator is stopping at
	 * @param peopleExiting indicates if people are exiting the elevator
	 */
	public void handleReachedDestination(Elevator elevator, int floorNum, boolean peopleExiting) {
		try {
			reachedFloor(elevator);
			setNewState(ElevatorEvent.Direction.STOPPED.toString(), elevator);
			if (peopleExiting) turnButtonOff(floorNum, elevator);
			if (shouldSleep) Thread.sleep(TIME_REACH_FLOOR_BEFORE_DOORS_OPEN);
			doorsOpening(elevator);
			if (peopleExiting) {
				System.out.println(Thread.currentThread().getName() + ": people have exited from the elevator");
			}
			if (shouldSleep)
				Thread.sleep(TIME_DOORS_OPEN);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Update console and GUI when elevator reaches a floor.
	 * 
	 * @param elevator
	 */
	private void reachedFloor(Elevator elevator) {
		System.out.println(Thread.currentThread().getName() + ": elevator reached floor " + elevator.getCurrentFloor());
		elevator.getElevatorPanel().removeStar(elevator.getCurrentFloor());
	}
	
	/**
	 * Update console and GUI when elevator button is turned off 
	 * when it reaches the destination.
	 * 
	 * @param floorNum 		of destination
	 * @param elevator
	 */
	private void turnButtonOff(int floorNum, Elevator elevator) {
		System.out.println(Thread.currentThread().getName() + ": Button " + floorNum + " Light is OFF");
		elevator.getElevatorPanel().unHighlightDestination(floorNum);
	}
	
	/**
	 * Update console and GUI when elevators door open.
	 * 
	 * @param elevator
	 */
	private void doorsOpening(Elevator elevator) {
		System.out.println(Thread.currentThread().getName() + ": elevator doors opening");
		elevator.getElevatorPanel().openDoorsLabel();
	}

	/**
	 * Sets the state of the elevator.
	 * 
	 * @param elevator the current elevator
	 * @param name     the name of the state to transition the elevator to
	 */
	private void setNewState(String name, Elevator elevator) {
		this.stateName = name;
		System.out.println(Thread.currentThread().getName() + ": elevator currently in state " + this.toString());
		elevator.getElevatorPanel().handleStateChange(name);
	}
	
	/**
	 * Closes the Elevator's doors
	 */
	private void closeDoors(Elevator elevator) {
		System.out.println(Thread.currentThread().getName() + ": elevator doors closing");
		elevator.getElevatorPanel().closeDoorsLabel();
	}

	/**
	 * Gets the String representation of the state.
	 */
	@Override
	public String toString() {
		return stateName + " STATE";
	}
}
