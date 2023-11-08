package no.ntnu.server;

import no.ntnu.greenhouse.GreenhouseSimulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GreenhouseServer {
    public static final int PORT_NUMBER = 1025;
    private BufferedReader socketReader;
    private GreenhouseSimulator greenhouseSimulator;
    private boolean isTcpServerRunning;
    private ArrayList<Socket> clients;

    public GreenhouseServer(GreenhouseSimulator greenhouseSimulator) {
        this.greenhouseSimulator = greenhouseSimulator;
        this.clients = new ArrayList<>();
    }

    /**
     * Start the TCP server for this greenhouse.
     * Some code was reused from previous SmartTV project.
     * @param port The port of the listening socket.
     */
    public void startServer(int port) {
        ServerSocket listeningSocket = openListeningSocket(port);
        System.out.println("Server listening on port " + port);
        if (listeningSocket != null) {
            isTcpServerRunning = true;
            while (isTcpServerRunning) {
                Socket clientSocket = acceptNextClientConnection(listeningSocket);
                if (clientSocket != null) {
                    System.out.println("New client connected from " + clientSocket.getRemoteSocketAddress());
                    clients.add(clientSocket);

                    ClientHandler clientHandler = new ClientHandler(clientSocket, greenhouseSimulator, this);
                    clientHandler.start();
                }
            }
        }
    }

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
}
