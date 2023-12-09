package no.ntnu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.prefs.NodeChangeEvent;
import java.util.prefs.NodeChangeListener;
import no.ntnu.communication.commands.GetListOfNodes;
import no.ntnu.communication.messages.SensorReadingMessage;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.communication.Message;
import no.ntnu.communication.Command;
import no.ntnu.communication.Message;
import no.ntnu.communication.MessageSerializer;
import no.ntnu.communication.commands.GetListOfNodeInfo;
import no.ntnu.communication.messages.SensorReadingMessage;
import no.ntnu.communication.messages.StateMessage;
import no.ntnu.greenhouse.Actuator;
import no.ntnu.greenhouse.GreenhouseSimulator;
import no.ntnu.greenhouse.Sensor;
import no.ntnu.greenhouse.SensorActuatorNode;
import no.ntnu.listeners.common.ActuatorListener;
import no.ntnu.listeners.greenhouse.NodeStateListener;
import no.ntnu.listeners.greenhouse.SensorListener;

/**
 * The ClientHandler class is responsible for managing the communication with a single client.
 * It reads client requests, executes corresponding commands, and sends responses back to the client.
 */
public class ClientHandler extends Thread implements NodeStateListener, NodeChangeListener,
    SensorListener, ActuatorListener {
  private BufferedReader socketReader;
  private ConnectionManager connectionManager;
  private final Socket clientSocket;
  private final GreenhouseSimulator simulator;
  private PrintWriter socketWriter;
  private boolean readyToReceive; //Is the control panel ready to receive readings or not

    /**
     * Constructs a ClientHandler with the specified clientSocket, GreenhouseSimulator, and GreenhouseServer.
     *
     * @param clientSocket The Socket representing the connection to the client.
     * @param simulator The GreenhouseSimulator associated with the server.
     */
    public ClientHandler(Socket clientSocket, GreenhouseSimulator simulator) {
        this.connectionManager = new ConnectionManager(clientSocket);
        this.clientSocket = clientSocket;
        this.simulator = simulator;
        readyToReceive = false;
    }

  /**
   * Initializes the input stream for the client socket.
   *
   * @return true if the stream initialization is successful, false otherwise.
   */
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

    /**
     * Executes the main logic of the ClientHandler thread. Reads client requests,
     * processes corresponding commands, and sends responses back to the client.
     */
    public void run() {
        while (!connectionManager.isConnected() || !connectionManager.initializeStreams()) {
            System.err.println("Connection lost. Reconnecting...");
            connectionManager.reconnect();
        }

        connectionManager.getSocketWriter();

    System.out.println("handling new client on " + Thread.currentThread().getName());

    Message response = null;
    do {
      Command clientCommand = readClientRequest();

      if (clientCommand == null) {
        break;
      }

            System.out.println("Recieved from client: " + clientCommand);

      if (clientCommand instanceof GetListOfNodeInfo) {
        readyToReceive = true;
      }

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

    /**
     * Reads and deserializes a client request from the input stream.
     *
     * @return The Command object representing the client request, or null if an exception occurs.
     */
    private Command readClientRequest() {
        Message clientCommand = null;
        try {
            String rawClientRequest = connectionManager.getSocketReader().readLine();
            System.out.println("Received from client: " + rawClientRequest);
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

    /**
     * Sends a response to the client by serializing and writing it to the output stream.
     *
     * @param response The Message object to be sent as a response to the client.
     */
    private void sendResponseToClient(Message response) {
            connectionManager.getSocketWriter().println(MessageSerializer.toString(response));
            System.out.println("Sent response to " + connectionManager.clientSocket.getRemoteSocketAddress());
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

  /**
   * Called when a sensor has new values (readings).
   *
   * @param nodeid The ID of the node that has new readings.
   * @param sensors A list of sensors having new values (readings)
   */
  @Override
  public void sensorsUpdated(String nodeid, List<Sensor> sensors) {
    if (readyToReceive) {
      sendResponseToClient(new SensorReadingMessage(nodeid, sensors));
    }
  }

  @Override
  public void actuatorUpdated(int nodeId, Actuator actuator) {
    if (readyToReceive) {
      sendResponseToClient(new StateMessage(nodeId, actuator.getId(), actuator.isOn()));
    }
  }
}