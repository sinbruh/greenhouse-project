package no.ntnu.server;

import no.ntnu.greenhouse.GreenhouseSimulator;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import no.ntnu.communication.Message;

/**
 *
 * The GreenhouseServer class manages the communication between the GreenhouseSimulator,
 * control panels, and nodes. It provides functionality to start and stop the greenhouse,
 * as well as to initiate and manage the TCP server for handling client connections.
 *
 */
public class GreenhouseServer {
    public static final int NODE_PORT = 1026;
    public static final int CONTROL_PANEL_PORT = 1025;
    private BufferedReader socketReader;
    private GreenhouseSimulator greenhouseSimulator;
    private boolean isTcpServerRunning;
    private ArrayList<Socket> controlPanels;
    private ArrayList<Socket> nodes;

    public GreenhouseServer(GreenhouseSimulator greenhouseSimulator) {
        this.greenhouseSimulator = greenhouseSimulator;
        this.nodes = new ArrayList<>();
        this.controlPanels = new ArrayList<>();
        startGreenhouse();
    }

    /**
     * Initializes and starts the greenhouse.
     */
    public void startGreenhouse() {
        greenhouseSimulator.initialize();
        greenhouseSimulator.setServer(this);
        greenhouseSimulator.start();
    }

    /**
     * Start the TCP server for this greenhouse.
     * Some code was reused from previous SmartTV project.
     * @param @controlPanelPort The port of the control panel listening socket.
     * @param @nodePort The port of the node listening socket.
     */
    public void startServer(int controlPanelPort, int nodePort) {
        ServerSocket controlPanelSocket = openListeningSocket(controlPanelPort);
        ServerSocket nodeSocket = openListeningSocket(nodePort);

        System.out.println("Server listening for node on port " + nodePort);
        if (controlPanelSocket != null) {
            isTcpServerRunning = true;
            while (isTcpServerRunning) {
                Socket clientSocket = acceptNextClientConnection(controlPanelSocket);
                if (clientSocket != null) {
                    System.out.println("New client connected from " + clientSocket.getRemoteSocketAddress());
                    controlPanels.add(clientSocket);

                    ClientHandler clientHandler = new ClientHandler(clientSocket, greenhouseSimulator, this);
                    clientHandler.start();
                }
            }
        }
    }

    /**
     * Opens a ServerSocket on specified port to listen for incoming client connection.
     *
     * @param port port The port on which the ServerSocket will listen for incoming connections.
     * @return A ServerSocket instance if successfully created, or null if an exception occurs.
     */
    private ServerSocket openListeningSocket(int port) {
        ServerSocket listeningSocket = null;
        try {
            listeningSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Failed to open listening socket: " + e.getMessage());
        }
        return listeningSocket;
    }

    /**
     * Accepts the next incoming client connection on the provided ServerSocket.
     * If the acceptance of the connection fails, an error message is printed to the standard error stream.
     *
     * @param listeningSocket The ServerSocket on which to accept the next client connection.
     * @return A Socket representing the accepted client connection if successful, or null if an exception occurs.
     */
    private Socket acceptNextClientConnection(ServerSocket listeningSocket) {
        Socket clientSocket = null;
        try {
            clientSocket = listeningSocket.accept();
        } catch (IOException e) {
            System.err.println("Failed to open client socket: " + e.getMessage());
        }
        return clientSocket;
    }

    /**
     * Stops the TCP server.
     */
    private void stopServer() {
        isTcpServerRunning = false;
    }

    /**
     * Sends a message to all the clients currently connected to the server.
     */
  public void notifyAllClients(Message response) {
  }
}
