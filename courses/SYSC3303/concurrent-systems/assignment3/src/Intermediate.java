import java.net.*;
import java.util.Arrays;

/**
 * Intermediate Host that acts as a link between Client and Server,
 * sending and receiving messages between the two.
 */
public class Intermediate {

    private DatagramSocket clientSocket, serverSocket;
    private DatagramPacket clientPacket, serverPacket;


    /**
     * Host constructor.
     */
    public Intermediate() {
        try {
            clientSocket = new DatagramSocket(23);
            serverSocket = new DatagramSocket(68);
            serverSocket.setSoTimeout(10000);
            clientPacket = null;
            serverPacket = null;
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
     * @param other String of source or recipient of DatagramPacket being printed.
     */
    private void printPacket(DatagramPacket dp, boolean sending, String other) {
        String s;
        if (sending) {
            s = "INTERMEDIATE sending packet to ";
        }
        else {
            s = "INTERMEDIATE received packet from ";
        }
        System.out.println(s + other + " containing:");
        System.out.println("\tAs Bytes: " + Arrays.toString(dp.getData()) +
                "\n\tAs String: "+ new String(dp.getData(), 0, dp.getLength()) + "\n");
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
     * Receives data from client.
     */
    private synchronized void handleClientRequest(){
        while(clientPacket != null) {
            try {
                wait();
            } catch (InterruptedException e) {
                close(e);
            }
        }
        try {
            byte[] data = new byte[25];
            clientPacket = new DatagramPacket(data, data.length);
            clientSocket.receive(clientPacket);
            printPacket(clientPacket, false, "client");
            notifyAll();
        } catch (Exception e) {
            close(e);
        }
    }

    /**
     * Receives server response generated from client data.
     */
    private synchronized void handleServerResponse() {
        while(serverPacket != null) {
            try {
                wait();
            } catch (InterruptedException e) {
                close(e);
            }
        }
        try {
            byte[] data = new byte[4];
            serverPacket = new DatagramPacket(data, data.length);
            serverSocket.receive(serverPacket);
            printPacket(serverPacket, false, "server");
            notifyAll();
        } catch (Exception e ) {
            close(e);
        }

    }

    /**
     * Get client packet.
     *
     * @return  client packet
     */
    private synchronized DatagramPacket getClientPacket() {
        while(clientPacket == null){
            try {
                wait();
            } catch (InterruptedException e) {
                close(e);
            }
        }
        DatagramPacket temp = clientPacket;
        clientPacket = null;
        notifyAll();
        return temp;
    }

    /**
     * Get server packet.
     *
     * @return server packet
     */
    private synchronized DatagramPacket getServerPacket() {
        while(serverPacket == null){
            try {
                wait();
            } catch (InterruptedException e) {
                close(e);
            }
        }
        DatagramPacket temp = serverPacket;
        serverPacket = null;
        notifyAll();
        return temp;
    }

    /**
     * Program loop where the host receives a datagram from the client, echos it to
     * the server, waits for the server's response, and echos the server response
     * back to the client.
     */
    public void intermediateCommunication() {
        Thread clientThread = new Thread() {
            @Override
            public void run(){
                while(true) {
                    try {
                        //Receive datagram from client
                        handleClientRequest();

                        //Acknowledge client request
                        String s = "I got your request client";
                        DatagramPacket clientPacketAcknowledgment = new DatagramPacket(
                                s.getBytes(), s.getBytes().length,
                                InetAddress.getLocalHost(),
                                67
                        );
                        printPacket(clientPacketAcknowledgment, true, "CLIENT");
                        clientSocket.send(clientPacketAcknowledgment);

                        //Receive client request for server response
                        byte[] data = new byte[25];
                        DatagramPacket clientWantsResponsePacket = new DatagramPacket(data, data.length);
                        clientSocket.receive(clientWantsResponsePacket);
                        printPacket(clientWantsResponsePacket, false, "CLIENT");

                        //Send server response to client
                        DatagramPacket serverResponsePacket = getServerPacket();
                        serverResponsePacket.setAddress(InetAddress.getLocalHost());
                        serverResponsePacket.setPort(67);
                        clientSocket.send(serverResponsePacket);
                        printPacket(serverResponsePacket, true, "CLIENT");

                    } catch (Exception e) {
                        close(e);
                    }
                }
            }
        };

        Thread serverThread = new Thread() {
            @Override
            public void run(){
                while (true) {
                    try {
                        //Receive server request for client data
                        byte[] data = new byte[15];
                        DatagramPacket serverWantsClientPacket = new DatagramPacket(data, data.length);
                        serverSocket.receive(serverWantsClientPacket);
                        printPacket(serverWantsClientPacket, false, "SERVER");

                        //Reply to server with client data
                        DatagramPacket clientRequestPacket = getClientPacket();
                        clientRequestPacket.setAddress(InetAddress.getLocalHost());
                        clientRequestPacket.setPort(serverWantsClientPacket.getPort());
                        printPacket(clientRequestPacket, true, "SERVER");
                        serverSocket.send(clientRequestPacket);

                        //Receive server response
                        handleServerResponse();

                        //Acknowledge server response
                        String s = "I got your response server";
                        DatagramPacket serverPacketAcknowledgment = new DatagramPacket(
                                s.getBytes(), s.getBytes().length,
                                InetAddress.getLocalHost(),
                                69
                        );
                        printPacket(serverPacketAcknowledgment, true, "SERVER");
                        serverSocket.send(serverPacketAcknowledgment);
                    }
                    catch (Exception e) {
                        close(e);
                    }
                }
            }
        };

        clientThread.start();
        serverThread.start();
    }

    /**
     * Main function that instantiates an intermediate host and runs it.
     *
     * @param args
     */
    public static void main(String[] args) {
        Intermediate i = new Intermediate();
        i.intermediateCommunication();
    }
}