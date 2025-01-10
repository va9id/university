/**
 * 
 */
package com.sysc3303.project.scheduler;

/**
 * Class to represent the scheduler's state; superclass with default behavior to events
 * @author Group 9
 *
 */
public class SchedulerState {
	public void handleResponseReceived(Scheduler scheduler) {
		System.out.println("Cannot call 'handleResponseReceived' in this state!");
	}
	
	public void handleResponseProcessed(Scheduler scheduler) {
		System.out.println("Cannot call 'handleResponseProcessed' in this state!");
	}
}
