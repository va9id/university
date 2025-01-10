import java.net.*;
import java.util.Arrays;

/**
 * Client that sends requests to the intermediate Host.
 */
public class Client {

    private String clientName;
    private DatagramSocket socket;

    /**
     * Client constructor.
     *
     * @param name  String name of Client.
     */
    public Client(String name){
        this.clientName = name;
        try {
            socket = new DatagramSocket(67);
            socket.setSoTimeout(10000);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Generates the client request's Datagram's data as a byte array
     *
     * @param read  true if DatagramPacket is read, false if it's write.
     * @return  byte array of the Datagram's data.
     */
    private byte[] generateData(boolean read) {
        byte readOrWrite;
        if (read) {
            readOrWrite = 1;
        }
        else {
            readOrWrite = 2;
        }
        byte zero = 0;
        String filename = "vahid.txt";
        byte[] filenameAsBytes = filename.getBytes();
        String mode = "netascii";
        byte[] modeAsBytes = mode.getBytes();
        byte[] msg = new byte[4 + filenameAsBytes.length + modeAsBytes.length];

        msg[0] = zero;
        msg[1] = readOrWrite;
        int i = 2;
        for (byte b: filenameAsBytes) {
            msg[i] = b;
            i += 1;
        }
        msg[i] = zero;
        i += 1;
        for(byte b: modeAsBytes) {
            msg[i] =b;
            i += 1;
        }
        msg[i] = zero;
        return msg;
    }

    /**
     * Helper method to print to the console Datagrams that are sent
     * from server or received by it.
     *
     * @param dp    DatagramPacket being printed.
     * @param sending   true if datagram is being sent from the server, false if it's being received.
     */
    private void printPacket(DatagramPacket dp, boolean sending){
        String s;
        if (sending) {
            s = " sending packet to ";
        }
        else {
            s = " received packet from ";
        }
        System.out.println(clientName + s + "INTERMEDIATE containing:");
        System.out.println("\tAs Bytes: " + Arrays.toString(dp.getData()) +
                "\n\tAs String: "+ new String(dp.getData(), 0, dp.getLength()) + "\n");
    }

    /**
     * Closes the Client's socket and terminates the program when an exception is thrown.
     */
    private void close(Exception e) {
        e.printStackTrace();
        socket.close();
        System.exit(1);
    }

    /**
     * Program loop where the client sends a datagram to the host, and waits to receive
     * the corresponding response form the host.
     */
    public void clientCommunication() {
        for (int i = 0; i < 11; i++) {
            boolean isRead = i % 2 == 0;
            byte[] msg = generateData(isRead);
            if (i == 10) { msg[0] = 1; } //Last packet will be invalid
            try {
                //Send client packet to host
                DatagramPacket sendPacket = new DatagramPacket(
                        msg,
                        msg.length,
                        InetAddress.getLocalHost(),
                        23);
                printPacket(sendPacket, true);
                socket.send(sendPacket);

                //Receive reply from Intermediate
                byte[] replyData = new byte[27];
                DatagramPacket receiveIntermediateReply = new DatagramPacket(replyData, replyData.length);
                socket.receive(receiveIntermediateReply);
                printPacket(receiveIntermediateReply, false);

                //Send request for server response from Intermediate
                String s = "give me my response";
                DatagramPacket sendRequestForDataPacket = new DatagramPacket(
                        s.getBytes(),
                        s.getBytes().length,
                        InetAddress.getLocalHost(),
                        23
                );
                printPacket(sendRequestForDataPacket, true);
                socket.send(sendRequestForDataPacket);

                //Receive server response packet from Intermediate
                byte[] data = new byte[4];
                DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                socket.receive(receivePacket);
                printPacket(receivePacket, false);

            } catch (SocketTimeoutException se) {
                System.out.println(clientName + " timed out after not receiving a response from INTERMEDIATE after 10s\n");
                close(se);
            }
            catch(Exception e) {
                close(e);
            }
        }
        socket.close();
    }

    /**
     * Main function that instantiates an intermediate host and runs it.
     *
     * @param args
     */
    public static void main(String args[]) {
        Client c = new Client("CLIENT");
        c.clientCommunication();
    }
}
