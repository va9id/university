import java.net.*;
import java.util.Arrays;
import java.util.Locale;

/**
 * Intermediate Host that acts as a link between Client and Server,
 * sending and receiving messages between the two.
 */
public class Host {

    private DatagramSocket clientSocket, serverSocket;

    /**
     * Host constructor.
     */
    public Host() {
        try {
            clientSocket = new DatagramSocket(23);
            serverSocket = new DatagramSocket();
            serverSocket.setSoTimeout(10000);
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Helper method to print to the console Datagrams that are sent
     * from server or received by it.
     *
     * @param dp    DatagramPacket being printed.
     * @param sending   true if datagram is being sent from the server, false if it's being received.
     * @param destination String of destination of the DatagramPacket being printed.
     */
    private void printPacket(DatagramPacket dp, boolean sending, String destination) {
        String send1, send2, send3;
        destination = destination.toUpperCase(Locale.ROOT);
        if (sending) {
            send1 = "sending packet to " + destination;
            send2 = "To " + destination + ": ";
            send3 = "Destination of " + destination + " port: ";
        }
        else {
            send1 = "received packet from " + destination;
            send2 = "From " + destination + ": ";
            send3 = destination + " port: ";
        }
        System.out.println("Host: " + send1);
        System.out.println("\t" + send2 + dp.getAddress());
        System.out.println("\t" + send3 + dp.getPort());
        System.out.println("\tLength: " + dp.getLength());
        System.out.println("\tContaining: " +
                "\n\t\tAs Bytes: " + Arrays.toString(dp.getData()) +
                "\n\t\tAs String: "+ new String(dp.getData(),0, dp.getLength()) + "\n"
        );
    }

    /**
     * Closes the Host's sockets and terminates the program when an exception is thrown.
     */
    private void close(Exception e) {
        e.printStackTrace();
        serverSocket.close();
        clientSocket.close();
        System.exit(1);
    }

    /**
     * Program loop where the host receives a datagram from the client, echos it to
     * the server, waits for the server's response, and echos the server response
     * back to the client.
     */
    public void hostCommunication() {
        while(true) {
            try {
                //Receive datagram from client
                byte[] clientData = new byte[25];
                DatagramPacket clientReceivePacket = new DatagramPacket(clientData, clientData.length);
                System.out.println("Host: waiting for request from CLIENT...\n");
                clientSocket.receive(clientReceivePacket);
                printPacket(clientReceivePacket, false, "client");

                //Send client datagram to server
                DatagramPacket serverSendPacket = new DatagramPacket(
                        clientReceivePacket.getData(),
                        clientReceivePacket.getLength(),
                        InetAddress.getLocalHost(), 69);
                printPacket(serverSendPacket, true, "server");
                serverSocket.send(serverSendPacket);
                System.out.println("Host: CLIENT packet sent to SERVER\n");

                //Receive datagram sent from server
                byte[] serverData = new byte[4];
                DatagramPacket serverReceivePacket = new DatagramPacket(serverData, serverData.length);
                System.out.println("Host: waiting for response from SERVER...\n");
                serverSocket.receive(serverReceivePacket);
                printPacket(serverReceivePacket, false, "server");

                //Send datagram received from server to client using new socket
                DatagramPacket clientSendPacket = new DatagramPacket(
                        serverData,
                        serverReceivePacket.getLength(),
                        InetAddress.getLocalHost(),
                        clientReceivePacket.getPort());
                printPacket(clientSendPacket, true, "client");
                DatagramSocket tempSocket = new DatagramSocket();
                tempSocket.send(clientSendPacket);
                System.out.println("Host: SERVER packet sent to CLIENT\n");
                tempSocket.close();

            } catch (SocketTimeoutException se) {
                System.out.println("HOST timed out (no packet from CLIENT/SERVER in 10s)\n");
                close(se);
            }
            catch (Exception e) {
                close(e);
            }
        }
    }

    /**
     * Main function that instantiates an intermediate host and runs it.
     *
     * @param args
     */
    public static void main(String[] args) {
        Host h = new Host();
        h.hostCommunication();
    }
}
