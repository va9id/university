/**
 * 
 */
package com.sysc3303.project.scheduler;

/**
 * @author Group 9
 * Class representing the state of the scheduler when it can receive and respond to requests (i.e. there are queued responses)
 */
public class SchedulerReceivingSendingState extends SchedulerState {
	
	/**
	 * Handles the event when a response is received from an Elevator
	 * 
	 * @param scheduler the current scheduler
	 */
	@Override
	public void handleResponseReceived(Scheduler scheduler) {
		System.out.println(Thread.currentThread().getName() + ": received response from Elevator");
	}
	
	/**
	 * Handles the event when a response is processed (i.e. can be sent to Floor)
	 * 
	 * @param scheduler the current scheduler
	 */
	@Override
	public void handleResponseProcessed(Scheduler scheduler) {
		System.out.println(Thread.currentThread().getName() + ": sending response to Floor");
		if (!scheduler.isResponseInQueue()) {
			scheduler.setState(new SchedulerReceivingState()); // transition to sending state
			System.out.println(Thread.currentThread().getName() + ": scheduler currently in state "  + scheduler.getState());
		}
	}
	
	/**
	 * Gets the String representation of the state.
	 */
	@Override
	public String toString() {
		return "RECEIVING AND SENDING STATE";
	}
}
