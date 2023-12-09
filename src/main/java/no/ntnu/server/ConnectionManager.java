package no.ntnu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionManager {
    Socket clientSocket;
    private BufferedReader socketReader;
    private PrintWriter socketWriter;

    public ConnectionManager(Socket clientSocket) {
        this.clientSocket = clientSocket;
        initializeStreams();
    }

    boolean initializeStreams() {
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            return true;
        } catch (IOException e) {
            System.err.println("Could not initialize streams");
            return false;
        }
    }

    public boolean isConnected() {
        return clientSocket != null && !clientSocket.isClosed();
    }

    public void reconnect() {
        System.out.print("Reconnecting to server...");
        while (!initializeStreams() && isConnected()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Could not reconnect to server");
            }
        }
        System.out.println("Reconnected to server");
    }

    public void closeConnection() {
        try {
            clientSocket.close();
            socketReader.close();
            socketWriter.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            System.err.println("Could not close connection");
        }
    }

    public BufferedReader getSocketReader() {
        return socketReader;
    }

    public PrintWriter getSocketWriter() {
        return socketWriter;
    }
}
