package no.ntnu.server;

import no.ntnu.greenhouse.GreenhouseSimulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import no.ntnu.communication.Message;

public class GreenhouseServer {
    public static final int PORT_NUMBER = 1025;
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
     * @param port The port of the listening socket.
     */
//    public void startServer(int nodePort, int controlPanelPort) {
//        ServerSocket nodeSocket = openListeningSocket(nodePort);
//        ServerSocket controlPanelSocket = openListeningSocket(controlPanelPort);
//
//        System.out.println("Server listening for node on port " + nodePort);
//        if (listeningSocket != null) {
//            isTcpServerRunning = true;
//            while (isTcpServerRunning) {
//                Socket clientSocket = acceptNextClientConnection(listeningSocket);
//                if (clientSocket != null) {
//                    System.out.println("New client connected from " + clientSocket.getRemoteSocketAddress());
//                    clients.add(clientSocket);
//
//                    ClientHandler clientHandler = new ClientHandler(clientSocket, greenhouseSimulator, this);
//                    clientHandler.start();
//                }
//            }
//        }
//    }

    private ServerSocket openListeningSocket(int port) {
        ServerSocket listeningSocket = null;
        try {
            listeningSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Failed to open listening socket: " + e.getMessage());
        }
        return listeningSocket;
    }

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
