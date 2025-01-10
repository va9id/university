package com.sysc3303.project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import com.sysc3303.project.elevator.ElevatorEvent.Direction;
import com.sysc3303.project.elevator.ElevatorState;
import com.sysc3303.project.floor.Floor;

/**
 * ElevatorPanel that shows all the associated components for each Elevator  
 * 
 * @author Group 9
 */
public class ElevatorPanel extends JPanel {
	private final int WIDTH = 125;
	private final int HEIGHT = 600;
	private JLabel floorLabel;
	public JSlider slider;
	private JLabel doorsLabel;
	private JLabel statusLabel;
	private final static Color GREEN_COLOR = new Color(0, 192, 0);
	private final static Color RED_COLOR = new Color(255, 48, 0);
	private final static Color YELLOW_COLOR = new Color(255, 193, 70);
	private final static String UP_ICON  = "Resources/images/upArrowIcon.png";
	private final static String DOWN_ICON  = "Resources/images/downArrowIcon.png";
	private final static String TRANS_FAULT_ICON  = "Resources/images/transientFaultIcon.png";
	private final static String HARD_FAULT_ICON  = "Resources/images/hardFaultIcon.png";
	private final static String DOORS_OPENS = "[|   |]";
	private final static String DOORS_CLOSED = "[|]";
	    
		
	/**
	* Constructor for the ElevatorPanel object.
	*/
	public ElevatorPanel() {
		super();
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.black);
		initializeComponents();
	}
	
	/**
	 * Initialize components to the Elevator JPanel.
	 */
	private void initializeComponents() {
		createFloorLabel();
		createSlider();
		createDoorLabel();
		createStatusLabel();
		this.add(floorLabel);
		this.add(slider);
		this.add(doorsLabel);
		this.add(statusLabel);
	}
	
	/**
	 * Create JSlider component that shows the Elevator moving through the floors. 
	 */
	private void createSlider() {
		slider = new JSlider(SwingConstants.VERTICAL, Floor.BOTTOM_FLOOR, Floor.NUM_FLOORS, Floor.BOTTOM_FLOOR);
		slider.setPreferredSize(new Dimension(WIDTH, HEIGHT - 150));
		slider.setBackground(Color.black);
		slider.setMajorTickSpacing(1);
	    slider.setPaintLabels(true);
	    slider.setPaintTicks(true);
	    
	    slider.removeMouseListener(slider.getMouseListeners()[0]);
	    slider.setFocusable(false);
	    
	    Hashtable<Integer, JLabel> table = new Hashtable<>();
	    
	    for (int i = Floor.BOTTOM_FLOOR; i <= Floor.NUM_FLOORS; i++) {
	    	JLabel label = new JLabel(String.valueOf(i));
	    	label.setForeground(Color.white);
	    	label.setPreferredSize(new Dimension(
	    			label.getPreferredSize().width + 10, label.getPreferredSize().height));
	    	label.setFont(new Font("Aharoni", 0, 13));
	    	table.put(i, label);
	    }
	    slider.setLabelTable(table);
	}
	
	/**
	 * Create label showing the current floor of the elevator with icons indicating
	 * the direction it's going or if a fault occurs. 
	 */
	private void createFloorLabel() {
		floorLabel = new JLabel(String.valueOf(Floor.BOTTOM_FLOOR));
		floorLabel.setFont(new Font("Aharoni", 0, 22));
		floorLabel.setPreferredSize(new Dimension(WIDTH, 35));
		floorLabel.setForeground(Color.white);
		floorLabel.setIconTextGap(5);
		floorLabel.setVerticalAlignment(JLabel.CENTER);
		floorLabel.setHorizontalAlignment(JLabel.CENTER);
	}
	
	/**
	 * Create door label that indicates when the elevator doors are open or closed. 
	 */
	private void createDoorLabel() {
		doorsLabel = new JLabel(DOORS_OPENS);
		doorsLabel.setFont(new Font("Aharoni", 0, 20));
		doorsLabel.setPreferredSize(new Dimension(WIDTH, 35));
		doorsLabel.setForeground(Color.white);
		doorsLabel.setVerticalAlignment(JLabel.CENTER);
		doorsLabel.setHorizontalAlignment(JLabel.CENTER);
	}
	
	/**
	 * Creates the status label which indicates the status of the elevator.
	 */
	private void createStatusLabel() {
		statusLabel = new JLabel("<html><div style='text-align: center;'>Waiting for request</div></html>");
		statusLabel.setFont(new Font("Aharoni", 0, 14));
		statusLabel.setPreferredSize(new Dimension(WIDTH - 10, 70));
		statusLabel.setForeground(Color.white);
		statusLabel.setVerticalAlignment(JLabel.CENTER);
		statusLabel.setHorizontalAlignment(JLabel.CENTER);
	}
	
	/**
	 * Changes the Door Label to closed.
	 */
	public void closeDoorsLabel() {
		doorsLabel.setText(DOORS_CLOSED);
	}
	
	/**
	 * Changes the Door Label to open. 
	 */
	public void openDoorsLabel() {
		doorsLabel.setText(DOORS_OPENS);
	}
	
	/**
	 * Update Floor Label with respective icon and color.  
	 * 
	 * @param path	String path to corresponding icon
	 * @param color	Color of label
	 */
	private void updateFloorLabel(String path, Color color) {
		floorLabel.setIcon(new ImageIcon(path));
		floorLabel.setForeground(color);
	}
	
	/**
	 * Handles state change 
	 * 
	 * @param state	String corresponding to corresponding ElevatorState
	 */
	public void handleStateChange(String state) {
		if (state.equals(Direction.UP.toString())) {
			handleGoingUpState();
		}
		else if (state.equals(Direction.DOWN.toString())) {
			handleGoingDownState();
		}
		else if (state.equals(Direction.STOPPED.toString())) {
			handleStopped();
		}
		else if (state.equals(ElevatorState.REPAIRING_STATE_NAME)){
			handleTransientFault();
		} 
	}
	
	/**
	 * Updated floor label and icon when going up. 
	 */
	private void handleGoingUpState() {
		updateFloorLabel(UP_ICON, GREEN_COLOR);
	}
	
	/**
	 * Updated floor label and icon when going down. 
	 */
	private void handleGoingDownState() {
		updateFloorLabel(DOWN_ICON, RED_COLOR);
	}
	
	/**
	 * Updated slider when elevator is going up. 
	 */
	public void goingUp() {
		int floorNum = slider.getValue() + 1;
		slider.setValue(floorNum);
		floorLabel.setText(String.valueOf(floorNum));
	}
	
	/**
	 * Updated slider when elevator is going down. 
	 */
	public void goingDown() {
		int floorNum = slider.getValue() - 1;
		slider.setValue(floorNum);
		floorLabel.setText(String.valueOf(floorNum));

	}
	
	/**
	 * Updated floor label when elevator is in stopped state. 
	 */
	private void handleStopped() {
		floorLabel.setIcon(null);
		floorLabel.setForeground(Color.white);
	}
	
	/**
	 * Indicate transient fault in the floor label.  
	 */
	private void handleTransientFault() {
		updateFloorLabel(TRANS_FAULT_ICON, YELLOW_COLOR);
		updateStatus("Transient fault, repairing...");
	}
	
	/**
	 * Indicates that transient fault has been fixed  
	 */
	public void handleTransientFaultFixed() {
		floorLabel.setIcon(null);
		floorLabel.setForeground(Color.white);
		updateStatus("Transient fault fixed.");
	}
	
	/**
	 * Indicate hard fault in elevator. 
	 */
	public void handleHardFault() {
		updateFloorLabel(HARD_FAULT_ICON, RED_COLOR);
		slider.setForeground(RED_COLOR);
	    for (int i = Floor.BOTTOM_FLOOR; i <= Floor.NUM_FLOORS; i++) {
	    	((JLabel) slider.getLabelTable().get(i)).setForeground(RED_COLOR);
	    }
	    updateStatus("ERROR: hard fault");
	}
	
	/**
	 * Indicate elevator has finished servicing all
	 * its requests and is waiting for a new request
	 */
	public void handleFinishedMoving() {
		updateStatus("Waiting for request");
	}
	
	
	/**
	 * Highlight destination of floor request on the elevator's slider. 
	 * 
	 * @param floorNum destination of floor request
	 */
	public void highlightDestination(int floorNum) {
		((JLabel) slider.getLabelTable().get(floorNum)).setForeground(Color.cyan);
		updateStatus("New destination floor " + floorNum);
		slider.repaint();
	}
	
	/**
	 * Remove highlight from the destination of the elevator's request when you 
	 * arrive there. 
	 * 
	 * @param floorNum destination of floor request
	 */
	public void unHighlightDestination(int floorNum) {
		((JLabel) slider.getLabelTable().get(floorNum)).setForeground(Color.white);
		slider.repaint();
	}
	
	/**
	 * Add a star beside the floor number on the elevator's slider indicating that 
	 * someone has requested the elevator on that floor. 
	 * 
	 * @param floorNum floor someone is waiting for the elevator at
	 */
	public void addStar(int floorNum) {
		((JLabel) slider.getLabelTable().get(floorNum)).setText(floorNum + " *");
		updateStatus("Received request at floor " + floorNum);
		slider.repaint();
	}
	
	/**
	 * Remove star from floor number on the elevator's slider when you arrive to
	 * pick up the person waiting for the elevator. 
	 * 
	 * @param floorNum where person is waiting for the elevator
	 */
	public void removeStar(int floorNum) {
		((JLabel) slider.getLabelTable().get(floorNum)).setText(String.valueOf(floorNum));
		slider.repaint();
	}
	
	/**
	 * Updates the stauts label of elevator.
	 * 
	 * @param text The text to update the label with.
	 */
	private void updateStatus(String text) {
		statusLabel.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
	}
	
}
