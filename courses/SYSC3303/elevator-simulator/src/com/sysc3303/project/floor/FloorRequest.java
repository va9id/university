/**
 * 
 */
package com.sysc3303.project.floor;

import java.io.Serializable;

import com.sysc3303.project.elevator.ElevatorEvent;

/**
 * Represents a request sent from the Floor to the Scheduler
 * 
 * @author Group 9
 *
 */
public class FloorRequest implements Serializable {
	private final ElevatorEvent elevatorEvent;

	/**
	 * Constructor
	 * 
	 * @param elevatorEvent The ElevatorEvent object that it is sending
	 */
	public FloorRequest(ElevatorEvent elevatorEvent) {
		if (elevatorEvent == null) {
			throw new IllegalArgumentException("The ElevatorEvent object cannot be null");
		}
		this.elevatorEvent = elevatorEvent;
	}

	/**
	 * Checks if a FloorRequest denotes that all meaningful requests have been processed
	 * @return true if this request is the last one, false otherwise
	 */
	public boolean isEndOfRequests() {
		return elevatorEvent.isEndOfRequestsEvent();
	}

	/**
	 * @return the ElevatorEvent object of the request
	 */
	public ElevatorEvent getElevatorEvent() {
		return elevatorEvent;
	}
}
