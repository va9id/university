package com.sysc3303.project.elevator;

import java.io.Serializable;

import com.sysc3303.project.elevator.ElevatorEvent.Direction;

/**
 * Class to represent a request made by an Elevator.
 * 
 * @author Group 9
 */
public class ElevatorRequest implements Serializable {
	private final int floor;
	private final Direction direction;
	
	
	/**
	 * Constructor; initializes ElevatorRequest with current floor and direction.
	 * 
	 * @param currentFloor the int floor the Elevator is on.
	 * @param currentDirection the direction the ELevator is going in.
	 */
	public ElevatorRequest(int floor, Direction direction) {
		this.floor = floor;
		this.direction = direction;
	}
	
	/**
	 * Gets the floor of the Elevator request.
	 * 
	 * @return The floor number.
	 */
	public int getFloor() {
		return floor;
	}
	
	/**
	 * Gets the direction of the Elevator request.
	 * 
	 * @return The direction of the request.
	 */
	public Direction getDirection() {
		return direction;
	}
	
}
