/**
 * 
 */
package com.sysc3303.project.scheduler;

/**
 * Class representing the state of the scheduler when it is only receiving requests (i.e. no queued responses)
 * @author Group 9
 *
 */
public class SchedulerReceivingState extends SchedulerState {
	

	/**
	 * Handles the event when a response is received from an Elevator
	 * 
	 * @param scheduler the current scheduler
	 */
	@Override
	public void handleResponseReceived(Scheduler scheduler) {
		System.out.println(Thread.currentThread().getName() + ": received response from Elevator");
		scheduler.setState(new SchedulerReceivingSendingState()); // transition to receiving/sending state
		System.out.println(Thread.currentThread().getName() + ": scheduler currently in state "  + scheduler.getState());
	}
	
	/**
	 * Gets the String representation of the state.
	 */
	@Override
	public String toString() {
		return "RECEIVING STATE";
	}
}
