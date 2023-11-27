package no.ntnu.server;

import java.io.PrintWriter;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.communication.Message;
import no.ntnu.communication.Command;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import no.ntnu.communication.MessageSerializer;

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

            if (response != null) {
                sendResponseToClient(response);
            }

            //TODO, if the message is something all clients need to be notified of,
            //then use notify clients method in server class



            System.out.println("Recieved from client: " + clientCommand);
        } while (response != null);
    }

    private Command readClientRequest() {
        Message clientCommand = null;
        try {
            String rawClientRequest = socketReader.readLine();
            System.out.println("Recieved from client: " + rawClientRequest);
            clientCommand = MessageSerializer.fromString(rawClientRequest);
            if (!(clientCommand instanceof Command)) {
                System.err.println("Invalid message recieved");
                clientCommand = null;
            }
        } catch (IOException e) {
            System.err.println("Could not receive client request: " + e.getMessage());
        }
        return (Command) clientCommand;
    }
 
    private void sendResponseToClient(Message response) {
        try {
            new PrintWriter(clientSocket.getOutputStream(), true)
                .println(MessageSerializer.toString(response));
            System.out.println("Sent response to " + clientSocket.getRemoteSocketAddress());
        } catch (IOException e) {
            System.err.println("IOException" + e.getMessage());
        }
    }

    private void notifyAllClients(Message response) {
        server.notifyAllClients(response);
    }
}