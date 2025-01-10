
package com.sysc3303.project.elevator;

import java.io.Serializable;

import com.sysc3303.project.floor.Floor;
import com.sysc3303.project.utils.Time;

/**
 * ElevatorEvents are objects representing the lines of input read in from the
 * Floor subsystem
 * 
 * @author Group 9
 *
 */
public class ElevatorEvent implements Serializable {
	public enum Direction {
		UP, DOWN, STOPPED;
	}
	
	public enum Fault {
		NO_FAULT(0), TRANSIENT_FAULT(1), HARD_FAULT(2), SHUTDOWN(-1);
		
		private int faultVal;
		
		private Fault(int faultNum) {
			faultVal = faultNum;
		}
		
		public static Fault getFaultEnum(int faultNum) {
			for (Fault fault: Fault.values()) {
				if (fault.faultVal == faultNum) {
					return fault;
				}
			}
			throw new IllegalArgumentException("Invalid fault number!");
		}
	}

	private final Time time;
	private final int floorNumber;
	private final Direction direction;
	private final int carButtonNumber;
	private final Fault fault;

	/**
	 * @param time      the Time object corresponding to the time of the elevator
	 *                  request
	 * @param floor     the floor on which the elevator request was made
	 * @param direction the direction of the elevator request
	 * @param carButton the car button (button on inside of elevator) that was
	 *                  pressed in the elevator request
	 * @throws IllegalArgumentException exception is thrown if the ElevatorEvent
	 *                                  being created is invalid
	 */
	public ElevatorEvent(Time time, int floor, Direction direction, int carButton, Fault fault) throws IllegalArgumentException {
		if (!isEventValid(time, floor, direction, carButton) && fault != Fault.SHUTDOWN) {
			throw new IllegalArgumentException("Cannot create this ElevatorEvent as it would be invalid!");
		}
		this.time = time;
		this.floorNumber = floor;
		this.direction = direction;
		this.carButtonNumber = carButton;
		this.fault = fault;
	}

	/**
	 * @param time      the Time object for the event
	 * @param floor     the floor on which the elevator request was made
	 * @param direction the direction of the elevator request
	 * @param carButton the car button (button on inside of elevator) that was
	 *                  pressed in the elevator request
	 * @return true if the event is valid, and false otherwise
	 */
	private static boolean isEventValid(Time time, int floor, Direction direction, int carButton) {
		return (time != null && floor > 0 && !(floor == 1 && direction == Direction.DOWN) && floor <= Floor.NUM_FLOORS
				&& !(floor == Floor.NUM_FLOORS && direction == Direction.UP) && carButton != floor && carButton > 0
				&& carButton <= Floor.NUM_FLOORS && direction != Direction.STOPPED
				&& !(direction == Direction.UP && floor > carButton)
				&& !(direction == Direction.DOWN && floor < carButton));
	}
	
	/**
	 * @return the time on which the elevator request was made
	 */
	public Time getTime() {
		return time; 
	}

	/**
	 * @return the floor number on which the elevator request was made
	 */
	public int getFloorNumber() {
		return floorNumber;
	}

	/**
	 * @return the direction of the elevator request
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @return the button number in the elevator that was pressed by the passenger
	 */
	public int getCarButton() {
		return carButtonNumber;
	}

	/**
	 * @param otherEvent the other elevator event to compare this event to
	 * @return true if this event occurs before the other event, and false otherwise
	 */
	public boolean eventOccursBefore(ElevatorEvent otherEvent) {
		return this.time.isTimeBefore(otherEvent.time);
	}

	/**
	 * Factory method for an ElevatorEvent that signifies that all requests have been sent
	 * @return ElevatorEvent that denotes the program should finish
	 */
	public static ElevatorEvent createEndOfRequestsEvent() {
		return new ElevatorEvent(null, 0, null, 0, Fault.SHUTDOWN);
	}

	/**
	 * Gets the fault enum of the ElevatorEvent.
	 * @return the fault enum of the ElevatorEvent
	 */
	public Fault getFault() {
		return fault;
	}
	
	/** Checks if an ElevatorEvent signifies that all requests have been sent
	 * @return true if the event is a valid final event, and false otherwise
	 */
	public boolean isEndOfRequestsEvent() {
		return this.fault == Fault.SHUTDOWN;
	}

	/**
	 * Enables ElevatorEvent objects to be printed
	 */
	@Override
	public String toString() {
		return String.format("ElevatorEvent[Time: %s, Floor: %s, Floor Button: %s, Car Button: %s, %s]", time.toString(),
				floorNumber, direction, carButtonNumber, fault);
	}

}
