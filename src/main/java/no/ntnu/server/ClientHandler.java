package no.ntnu.server;

import java.io.PrintWriter;
import java.util.List;
import java.util.prefs.NodeChangeEvent;
import java.util.prefs.NodeChangeListener;
import no.ntnu.communication.commands.ReadyCommand;
import no.ntnu.communication.messages.SensorReadingMessage;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.communication.Message;
import no.ntnu.communication.Command;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import no.ntnu.communication.MessageSerializer;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.listeners.greenhouse.NodeStateListener;
import no.ntnu.listeners.greenhouse.SensorListener;

public class ClientHandler extends Thread implements NodeStateListener, NodeChangeListener,
    SensorListener {
    private BufferedReader socketReader;
    private Socket clientSocket;
    private GreenhouseSimulator simulator;
    private PrintWriter socketWriter;
    private boolean readyToReceive; //Is the control panel ready to receive readings or not

    public ClientHandler(Socket clientSocket, GreenhouseSimulator simulator, GreenhouseServer server) {
        this.clientSocket = clientSocket;
        this.simulator = simulator;
        readyToReceive = true;
    }

    private boolean initializeStreams() {
        boolean success = true;
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
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
                response = clientCommand.execute(simulator);
                System.out.println("Response: " + response.messageAsString());
            } catch (Exception e) {
                System.err.println("Could not execute command: " + e.getMessage());
            }

            if (response != null) {
                sendResponseToClient(response);
            }

            System.out.println("Recieved from client: " + clientCommand);
        } while (response != null);
    }

    private Command readClientRequest() {
        Message clientCommand = null;
        try {
            String rawClientRequest = socketReader.readLine();
            System.out.println("Recieved from client: " + rawClientRequest);
            clientCommand = MessageSerializer.fromString(rawClientRequest);

            if (clientCommand instanceof ReadyCommand) {
                readyToReceive = true;
            }

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
            socketWriter.println(MessageSerializer.toString(response));
            System.out.println("Sent response to " + clientSocket.getRemoteSocketAddress());
    }

    @Override
    public void onNodeReady(SensorActuatorNode node) {

    }

    @Override
    public void onNodeStopped(SensorActuatorNode node) {

    }

    @Override
    public void childAdded(NodeChangeEvent evt) {

    }

    @Override
    public void childRemoved(NodeChangeEvent evt) {

    }

    @Override
    public void sensorsUpdated(String nodeID, List<Sensor> sensors) {
        if (readyToReceive) {
            sendResponseToClient(new SensorReadingMessage(nodeID, sensors));
        }
    }
}