package com.sysc3303.project.gui;

import java.awt.FlowLayout; 
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.sysc3303.project.elevator.Elevator;

/**
 * Class to create the JFrame that holds each ElevatorPanel in the GUI. 
 * 
 * @author Group 9
 */
public class GUI extends JFrame {
	
	/**
	 * Constructor for the GUI object.
	 */
	public GUI() {
		super("Elevator subsystem - Group #9");
		initializeJFrame();
	}
	
	/**
	 * Initializes the JFrame. 
	 */
	public void initializeJFrame() {
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new FlowLayout());
        this.addPanels();
        this.pack();
        this.setVisible(true);
	}
	
	/**
	 * Add the corresponding GUI ElevatorPanel from each Elevator of the system. 
	 * 
	 */
	public void addPanels() {
		for(Elevator e: Elevator.elevators) {
			this.add(e.getElevatorPanel());
		}
	}
	
}
