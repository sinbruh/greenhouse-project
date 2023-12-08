package no.ntnu.server;

import no.ntnu.greenhouse.GreenhouseSimulator;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import no.ntnu.communication.Message;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 *
 * The GreenhouseServer class manages the communication between the GreenhouseSimulator,
 * control panels, and nodes. It provides functionality to start and stop the greenhouse,
 * as well as to initiate and manage the TCP server for handling client connections.
 *
 */
public class GreenhouseServer {
    public static final int CONTROL_PANEL_PORT = 1025;
    private GreenhouseSimulator greenhouseSimulator;
    private boolean isTcpServerRunning;
    private ArrayList<Socket> controlPanels;
    private SSLServerSocketFactory socketFactory;
    private ServerSocket serverSocket;

    public GreenhouseServer(GreenhouseSimulator greenhouseSimulator){
        this.greenhouseSimulator = greenhouseSimulator;
        this.controlPanels = new ArrayList<>();
        startGreenhouse();

        socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        serverSocket = openListeningSocket(CONTROL_PANEL_PORT);
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
    public void startServer(int controlPanelPort) throws IOException {
        System.out.println("Server listening for control panels on port " + controlPanelPort);
            isTcpServerRunning = true;
            while (isTcpServerRunning) {
                SSLSocket clientSocket = acceptNextClientConnection(serverSocket);
                if (clientSocket != null) {
                    clientSocket.startHandshake();
                    System.out.println("New client connected from " + clientSocket.getRemoteSocketAddress());
                    controlPanels.add(clientSocket);

                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    greenhouseSimulator.getNodes().values().forEach(node -> node.addSensorListener(clientHandler));
                    clientHandler.start();
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
            listeningSocket = socketFactory.createServerSocket();
        } catch (IOException e) {
            //System.err.println("Failed to open listening socket: " + e.getMessage());
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
    private SSLSocket acceptNextClientConnection(ServerSocket listeningSocket) {
        SSLSocket clientSocket = null;
        try {
            clientSocket = (SSLSocket) listeningSocket.accept();
        } catch (IOException e) {
            if (clientSocket != null) {
                System.err.println("Failed to open client socket: " + e.getMessage());
            }
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
