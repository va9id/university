/**
 * 
 */
package com.sysc3303.project.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author Group 9
 * 
 * A utility class for UDP related functions
 *
 */
public class UDPUtil {
	public static final int RECEIVE_PACKET_LENGTH = 2048;
	private static final boolean DEBUG_SEND_RECEIVE = false; //sets whether the stack trace for sending/receiving packets will be printed
	
	/**
	 * @return a new datagram socket object that listens on an open port
	 */
	public static DatagramSocket createDatagramSocket() {
		try {
			return new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param port the port to create the socket on
	 * @return a new datagram socket object that listens on the given port
	 */
	public static DatagramSocket createDatagramSocket(int port) {
		try {
			return new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return the localhost InetAddress
	 */
	public static InetAddress getLocalHost() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets the InetAdress object corresponding to the given IP Address
	 * 
	 * @param ipAddress the IP Address of the machine
	 * @return the corresponding InetAdress object
	 */
	public static InetAddress getAddress(String ipAddress) {
		try {
			return InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param socket the socket to send the packet from
	 * @param packet the packet to send
	 */
	public static void sendPacket(DatagramSocket socket, DatagramPacket packet) {
		try {
			socket.send(packet);
		} catch (IOException e) {
			if (DEBUG_SEND_RECEIVE) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param socket the socket to receive the packet
	 * @param packet the packet that will be filled
	 */
	public static void receivePacket(DatagramSocket socket, DatagramPacket packet) {
		try {
			socket.receive(packet);
		} catch (IOException e) {
			if (DEBUG_SEND_RECEIVE) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Blocks until a packet received, but as opposed to receivePacket(), throws an exception so it can be handled
	 * when the socket that is waiting is closed
	 * @param socket the socket to receive the packet
	 * @param packet the packet that will be filled
	 */
	public static void receivePacketInterruptable(DatagramSocket socket, DatagramPacket packet) throws IOException {
		socket.receive(packet);
	}
	
	/**
	 * Serializes an object into an array of bytes
	 * 
	 * @param object the object to serialize
	 * @return the byte array corresponding to the object
	 */
	public static byte[] convertToBytes(Object object) {
	    try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(); ObjectOutputStream out = new ObjectOutputStream(byteStream)) {
	        out.writeObject(object);
	        return byteStream.toByteArray();
	    }
	    catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * @param bytes the bytes to convert into an object
	 * @param length the length of the byte array to process
	 * @return the object that has been created from the corresponding byte array
	 */
	public static Object convertFromBytes(byte[] bytes, int length) {
	    return convertFromBytes(Arrays.copyOfRange(bytes, 0, length));
	}
	
	/**
	 * @param bytes the bytes to convert into an object
	 * @return the object that has been created from the byte array
	 */
	public static Object convertFromBytes(byte[] bytes) {
	    try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes); ObjectInputStream in = new ObjectInputStream(byteStream)) {
	        return in.readObject();
	    } 
	    catch (StreamCorruptedException e) {
			System.exit(0);
			return null;
		}
	    catch (Exception e) {
	    	e.printStackTrace();
			return null;
		}
	}

}