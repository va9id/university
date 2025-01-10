package com.sysc3303.project.floor;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.Queue;

import com.sysc3303.project.elevator.ElevatorEvent;
import com.sysc3303.project.elevator.ElevatorEvent.Fault;
import com.sysc3303.project.scheduler.Scheduler;
import com.sysc3303.project.utils.Time;
import com.sysc3303.project.utils.UDPUtil;

/**
 * The Floor subsystem
 * 
 * @author Group 9
 *
 */
public class Floor implements Runnable {
	public static final int NUM_FLOORS = 22;
	public static final int BOTTOM_FLOOR = 1;
	public static final int PORT = 5552;
	public static final InetAddress ADDRESS = UDPUtil.getLocalHost();
	private static final String INPUT_FILE_PATH = "Resources/floor_input.txt";
	private final DatagramSocket receiveSocket, sendSocket;
	private final Queue<ElevatorEvent> eventQueue = new ArrayDeque<>();
	private final Queue<String> responseQueue = new ArrayDeque<>();
	private boolean shouldSleep = true; //flag to set whether the threads should sleep or not
	private long startTime;
	private int numEvents = 0; //number of events read from input file

	/**
	 * A constructor for a FloorSubsystem
	 */
	public Floor() {
		this(true);
	}
	
	/**
	 * A constructor that sets the shouldSleep flag
	 * 
	 * @param shouldSleep a flag indicating if the floor threads will sleep or not
	 */
	public Floor(boolean shouldSleep) {
		receiveSocket = UDPUtil.createDatagramSocket(PORT);
		sendSocket = UDPUtil.createDatagramSocket();
		this.shouldSleep = shouldSleep;
	}

	/**
	 * Closes the sockets associated with the object
	 */
	public void closeSockets() {
		receiveSocket.close();
		sendSocket.close();
	}
	
	/**
	 * The Floor thread's run method
	 */
	@Override
	public void run() {
		readFloorInputFile();

		Thread sendToScheduler = new Thread(() -> {
			Time currentTime, previousTime; 
			previousTime = eventQueue.peek().getTime(); 
			// Get start time
			startTime = System.nanoTime(); 
			// Send events to scheduler
			for (ElevatorEvent e : eventQueue) {
				currentTime = e.getTime(); 
				int milliseconds = previousTime.getTimeDifferenceInMS(currentTime);
				try {
					if (shouldSleep)
						Thread.sleep(milliseconds);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				sendFloorRequest(new FloorRequest(e));
				previousTime = currentTime; 
			
				receiveAcknowledgment();//Receive acknowledgment from Scheduler
		
			}
			sendSocket.close();
		}, "FloorThread-1");
		
		Thread receiveResponses = new Thread(() -> {
			for (int i = 0; i < eventQueue.size(); i++) {
				receiveResponse();
				System.out.println(Thread.currentThread().getName() + ": " + getLatestResponse());
			}
			outputPerformanceMeasure();
			sendEndOfRequestsNotice();
			receiveSocket.close();
		}, "FloorThread-2");

		sendToScheduler.start();
		receiveResponses.start();
	}

	/**
	 * @param responseMessage the response message from the elevator
	 */
	public synchronized void addResponse(String responseMessage) {
		responseQueue.add(responseMessage);
		notifyAll();
	}

	/**
	 * @return the response message at the front of the queue
	 */
	public synchronized String getLatestResponse() {
		while (responseQueue.isEmpty()) {
			try {
				wait(); // wait if there are no responses available
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return responseQueue.remove();
	}
	
	/**
	 * Receives a response packet from the Scheduler subsystem
	 */
	private void receiveResponse() {
		DatagramPacket responsePacket = new DatagramPacket(new byte[UDPUtil.RECEIVE_PACKET_LENGTH], UDPUtil.RECEIVE_PACKET_LENGTH);
		UDPUtil.receivePacket(receiveSocket, responsePacket);
		System.out.println(Thread.currentThread().getName() + ": Received packet from Scheduler");
		String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
		addResponse(response);
	}
	
	/**
	 * Receives an acknowledgement packet from the scheduler
	 */
	private void receiveAcknowledgment() {
		DatagramPacket acknowledgementPacket = new DatagramPacket(new byte[UDPUtil.RECEIVE_PACKET_LENGTH], UDPUtil.RECEIVE_PACKET_LENGTH);
		UDPUtil.receivePacket(sendSocket, acknowledgementPacket);
		System.out.print(Thread.currentThread().getName() + ": Received acknowledgment packet from Scheduler containing "
				+ new String(acknowledgementPacket.getData(), 0, acknowledgementPacket.getLength()) + "\n");
	}
		
	
	/**
	 * Sends a FloorRequest object to the Scheduler subsystem
	 * 
	 * @param request the FloorRequest to send
	 */
	private void sendFloorRequest(FloorRequest request) {
		byte[] data = UDPUtil.convertToBytes(request);
		DatagramPacket packet = new DatagramPacket(data, data.length, Scheduler.ADDRESS, Scheduler.FLOOR_REQUEST_PORT);
		UDPUtil.sendPacket(sendSocket, packet);
		System.out.println(Thread.currentThread().getName() + ": Sent packet to Scheduler containing " + request.getElevatorEvent().toString());
	}

	/**
	 * Communicates with the Scheduler that all FloorRequests have been sent
	 */
	private void sendEndOfRequestsNotice() {
		FloorRequest endRequest = new FloorRequest(ElevatorEvent.createEndOfRequestsEvent());
		byte[] data = UDPUtil.convertToBytes(endRequest);
		DatagramPacket packet = new DatagramPacket(data, data.length, Scheduler.ADDRESS, Scheduler.FLOOR_REQUEST_PORT);
		DatagramSocket endRequestSocket = UDPUtil.createDatagramSocket();
		UDPUtil.sendPacket(endRequestSocket, packet);
		endRequestSocket.close();
	}

	/**
	 * Reads the input file for elevator timings Parses input into a queue of
	 * elevator events
	 */
	private void readFloorInputFile() {
		BufferedReader br = null;
		try {
			String rootProjectPath = new File("").getAbsolutePath();
			br = new BufferedReader(new FileReader(rootProjectPath.concat("/" + INPUT_FILE_PATH)));
			String line = br.readLine(); // First line is just the column names

			String[] lineValues;
			
			// read remaining lines from the text file
			while ((line = br.readLine()) != null) {
				numEvents += 1;
				lineValues = line.split(" ");
				Time elevatorTime = Time.createFromTimeString((lineValues[0]));
				int elevatorFloor = Integer.parseInt(lineValues[1]);
				ElevatorEvent.Direction elevatorDirection = ElevatorEvent.Direction.UP;
				if (lineValues[2].equals("Down"))
					elevatorDirection = ElevatorEvent.Direction.DOWN;
				int elevatorButton = Integer.parseInt(lineValues[3]); //Read from input file all necessary info
				int faultNum = Integer.parseInt(lineValues[4]);
				ElevatorEvent elevatorEvent = new ElevatorEvent(elevatorTime, elevatorFloor, elevatorDirection,
						elevatorButton, Fault.getFaultEnum(faultNum)); //Create new instance of ElevatorEvent with info read from file
				eventQueue.add(elevatorEvent); //Add ElevatorEvent to Queue
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Entry point for the Floor subsystem
	 */
	public static void main(String[] args) {
		Thread floorThread = new Thread(new Floor(), "FloorSubsystem");
		floorThread.start();
	}
	
	private void outputPerformanceMeasure() {
		long performance = (System.nanoTime() - startTime) / 1000000;
		long averageResponseTime = performance/numEvents;
		System.out.println(Thread.currentThread().getName() + ": " + "scheduler subsystem performance measure (time to run entire input file): " + performance + "ms");
		System.out.println(Thread.currentThread().getName() + ": " + "average response time (average time to handle single event): " + averageResponseTime + "ms");
	}
}
