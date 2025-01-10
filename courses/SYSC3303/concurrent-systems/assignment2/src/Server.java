import java.net.*;
import java.util.Arrays;

/**
 * Server that receives requests from the intermediate Host.
 */
public class Server {

    private DatagramSocket socket;

    /**
     * Server constructor.
     */
    public Server() {
        try {
            socket = new DatagramSocket(69);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Parses the byte array to validate that it follows the correct format.
     *
     * @param msg byte array from the Datagram's data.
     * @return  true if byte array is in the correct format, false otherwise.
     */
    private boolean parsePacketData(byte[] msg){
        int indexEndOfFilename = findEndOfString(msg, 2);
        int indexEndOfMode = findEndOfString(msg, indexEndOfFilename + 1);
        boolean isRestOfDataEmpty = restOfDataIsEmpty(msg, indexEndOfMode + 1) ;
        return msg[0] == 0 &&
                (msg[1] == 1 || msg[1] == 2) &&
                msg[indexEndOfFilename] == 0 &&
                msg[indexEndOfMode] == 0 &&
                isRestOfDataEmpty;
    }

    /**
     * Finds the end of the string in the array of bytes.
     * Helper method for packetParser.
     *
     * @param msg   byte array from the Datagram's data.
     * @param pos   index of beginning of string in the byte array.
     * @return  index after the string ends.
     */
    private int findEndOfString(byte[] msg, int pos) {
        for(int i = pos; i < msg.length; i++) {
            if (msg[i] == 0) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    /**
     * Checks if remaining indices in the byte array are empty (==0).
     * Helper method for parsePacketData.
     *
     * @param msg   byte array from the Datagram's data.
     * @param pos   index to begin iterating through the byte array.
     * @return  true if the rest of the byte array is empty, false otherwise.
     */
    private boolean restOfDataIsEmpty(byte[] msg, int pos) {
        for(int i = pos; i < msg.length; i++) {
            if (msg[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if byte is read or write.
     *
     * @param msg   byte array from the Datagram's data.
     * @return  true if the byte is read, false otherwise.
     */
    private boolean readOrWrite(byte[] msg) {
        return msg[1] == 1;
    }

    /**
     * Generates the server response's Datagram's data as a byte array.
     *
     * @param read  true if return response is read, false if it's write.
     * @return  byte array from the Datagram's data.
     */
    private byte[] generateData(boolean read) {
        byte[] msg = new byte[4];
        msg[0] = msg[2] = 0;
        if (read) {
            msg[1] = 3;
            msg[3] = 1;
        } else {
            msg[1] = 4;
            msg[3] = 0;
        }
        return msg;
    }

    /**
     * Helper method to print to the console Datagrams that are sent
     * from server or received by it.
     *
     * @param dp    DatagramPacket being printed.
     * @param sending   true if datagram is being sent from the server, false if it's being received.
     */
    private void printPacket(DatagramPacket dp, boolean sending) {
        String send1, send2, send3;
        if (sending) {
            send1 = "sending packet to HOST";
            send2 = "Host address: ";
            send3 = "Host port: ";
        }
        else {
            send1 = "received packet from HOST";
            send2 = "Host address: ";
            send3 = "Host port: ";
        }
        System.out.println("Server: " + send1);
        System.out.println("\t" + send2 + dp.getAddress());
        System.out.println("\t" + send3 + dp.getPort());
        System.out.println("\tLength: " + dp.getLength());
        System.out.println("\tContaining: " +
                "\n\t\tAs Bytes: " + Arrays.toString(dp.getData()) +
                "\n\t\tAs String: "+ new String(dp.getData(),0, dp.getLength()) + "\n"
        );
    }

    /**
     * Program loop where the server waits to receive a datagram from the host, and sends
     * the corresponding response back to the host.
     *
     * @throws Exception if the server receives an invalid Datagram.
     */
    public void serverCommunication() throws Exception {
        while (true) {
            try {
                //Receive packet from host
                byte[] hostData = new byte[25];
                DatagramPacket receivePacket = new DatagramPacket(hostData, hostData.length);
                System.out.println("Server: waiting for response from HOST...\n");
                socket.receive(receivePacket);
                printPacket(receivePacket, false);
                hostData = receivePacket.getData();
                boolean isValid = parsePacketData(hostData);
                if (!isValid) {
                    socket.close();
                    throw new Exception("Server received an INVALID REQUEST");
                }

                //Send response packet back to host
                boolean isRead = readOrWrite(hostData);
                byte[] serverResponse = generateData(isRead);
                DatagramPacket sendPacket = new DatagramPacket(
                        serverResponse,
                        serverResponse.length,
                        InetAddress.getLocalHost(),
                        receivePacket.getPort());
                printPacket(sendPacket, true);
                DatagramSocket tempSocket = new DatagramSocket();
                tempSocket.send(sendPacket);
                System.out.println("Server: packet sent to HOST\n");
                tempSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
                socket.close();
                System.exit(1);
            }
        }
    }

    /**
     * Main function that instantiates a server and runs it.
     *
     * @param args
     */
    public static void main(String args[]) throws Exception {
        Server s = new Server();
        s.serverCommunication();
    }
}
