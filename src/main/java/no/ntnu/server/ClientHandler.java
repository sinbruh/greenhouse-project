package no.ntnu.server;

import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.messages.Message;
import no.ntnu.messages.Command;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler extends Thread {
    private BufferedReader socketReader;
    private Socket clientSocket;
    private GreenhouseSimulator simulator;
    private GreenhouseServer server;

    public ClientHandler(Socket clientSocket, GreenhouseSimulator simulator, GreenhouseServer server) {
        this.clientSocket = clientSocket;
        this.simulator = simulator;
        this.server = server;
    }

    private boolean initializeStreams() {
        boolean success = true;
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            success = false;
            System.err.println("Could not initialize streams");
        }
        return success;
    }

    public void run() {
        if (!initializeStreams()) {
            return;
        }

        System.out.println("handling new client on " + Thread.currentThread().getName());

        Message response = null;
        do {
            Command clientCommand = readClientRequest();

            if (clientCommand == null) {
                break;
            }

            System.out.println("Recieved from client: " + clientCommand);

            try {
                response = clientCommand.execute();
            } catch (Exception e) {
                System.err.println("Could not execute command: " + e.getMessage());
            }

            System.out.println("Recieved from client: " + clientCommand);
        } while (response != null);
    }

    private Command readClientRequest() {
        //TODO finish implementation
        return null;
    }
}
